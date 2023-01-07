package com.harana.designer.backend.services.apps

import com.harana.designer.backend.services.Crud
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.docker.Docker
import com.harana.modules.kubernetes.Kubernetes
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.Vertx
import com.harana.modules.vertx.Vertx.WebSocketHeaders
import com.harana.modules.vertx.models.Response
import com.harana.modules.vertx.proxy.WSURI
import com.harana.sdk.shared.models.apps.{App => DesignerApp}
import com.harana.sdk.shared.models.jwt.DesignerClaims
import io.circe.syntax._
import io.vertx.core.http.{Cookie, CookieSameSite}
import io.vertx.ext.web.RoutingContext
import skuber.LabelSelector.IsEqualRequirement
import skuber.apps.v1.Deployment
import skuber.json.format._
import skuber.{LabelSelector, Service, _}
import zio.clock.Clock
import zio.duration._
import zio.{Task, _}

import java.net.URI
import java.util.concurrent.atomic.AtomicReference

object LiveApps {
  val layer = ZLayer.fromServices { (clock: Clock.Service,
                                     config: Config.Service,
                                     docker: Docker.Service,
                                     jwt: JWT.Service,
                                     kubernetes: Kubernetes.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service,
                                     vertx: Vertx.Service) => new Apps.Service {

    def list(rc: RoutingContext): Task[Response]    = Crud.listResponse[DesignerApp]("Apps", rc, config, jwt, logger, micrometer, mongo)
    def tags(rc: RoutingContext): Task[Response]    = Crud.tagsResponse[DesignerApp]("Apps", rc, config, jwt, logger, micrometer, mongo)
    def owners(rc: RoutingContext): Task[Response]  = Crud.ownersResponse[DesignerApp]("Apps", rc, config, jwt, logger, micrometer, mongo)
    def search(rc: RoutingContext): Task[Response]  = Crud.searchResponse[DesignerApp]("Apps", rc, config, jwt, logger, micrometer, mongo)
    def get(rc: RoutingContext): Task[Response]     = Crud.getResponse[DesignerApp]("Apps", rc, config, jwt, logger, micrometer, mongo)
    def delete(rc: RoutingContext): Task[Response]  = Crud.deleteResponse[DesignerApp]("Apps", rc, config, jwt, logger, micrometer, mongo)
    def create(rc: RoutingContext): Task[Response]  = Crud.createResponse[DesignerApp]("Apps", rc, config, jwt, logger, micrometer, mongo)
    def update(rc: RoutingContext): Task[Response]  = Crud.updateResponse[DesignerApp]("Apps", rc, config, jwt, logger, micrometer, mongo)

    private val updateVersionsRef = new AtomicReference[Option[Fiber.Runtime[_, _]]](None)
    private val versions = new AtomicReference[Map[String, String]](Map.empty)


    def startup: Task[Unit] =
      for {
        _                 <- updateVersions()
        updateFiber       <- updateVersions().repeat(Schedule.spaced(60.minutes).forever).provideLayer(Clock.live).fork
        _                 =  updateVersionsRef.set(Some(updateFiber))
      } yield ()


    private def updateVersions() =
      for {
        images            <- config.listString("apps.images")
        tags              <- ZIO.foreach(images)(i => docker.hubTags("haranaoss", i, pageSize = Some(1)).map(t => (i, t.head)))
        _                 <- ZIO.foreach_(tags)(t => UIO(versions.set(versions.get() + (t._1 -> t._2.name))))
      } yield ()


    def shutdown: UIO[Unit] =
      for {
        updateFiber       <- UIO(updateVersionsRef.get.get)
        _                 <- updateFiber.interrupt.ignore
      } yield ()


    def updates(rc: RoutingContext): Task[Response] =
      Task(Response.JSON(content = versions.get().asJson))


    def connect(rc: RoutingContext): Task[Response] =
      for {
        claims            <- jwt.claims[DesignerClaims](rc)
        app               <- Crud.get[DesignerApp]("Apps", rc, config, jwt, logger, micrometer, mongo).map(_.head)
        name              =  serviceName(app, claims)

        appCookieName     =  s"app-${app.title.toLowerCase}"
        appCookie         <- startApp(app, claims, name, appCookieName)
        currentAppCookie  <- cookie("app-active", app.title.toLowerCase)

        response          =  Response.JSON(content = app.asJson, cookies = List(appCookie, currentAppCookie))
      } yield response


    def disconnect(rc: RoutingContext): Task[Response] =
      for {
        claims            <- jwt.claims[DesignerClaims](rc)
        app               <- Crud.get[DesignerApp]("Apps", rc, config, jwt, logger, micrometer, mongo).map(_.head)
        _                 <- logger.debug(s"Stopping app: ${app.title} for user: ${claims.emailAddress}")
        name              =  serviceName(app, claims)

        client            <- kubernetes.newClient
        namespace         <- config.string("kubernetes.namespace.apps")
        _                 <- kubernetes.delete[Service](client, namespace, name).ignore
        _                 <- kubernetes.delete[Deployment](client, namespace, name).ignore

        activeCookie      <- cookie("app-active", "").map(_.setMaxAge(0))
        appCookie         <- cookie(s"app-${app.title.toLowerCase}", "").map(_.setMaxAge(0))

        _                 <- kubernetes.close(client)
        response          =  Response.Empty(cookies = List(activeCookie, appCookie))
      } yield response


    def restart(rc: RoutingContext): Task[Response] =
      for {
        _                 <- disconnect(rc)
        response          <- connect(rc)
      } yield response


    def proxyHttp(rc: RoutingContext): Task[URI] =
      for {
        currentApp        <- Task(rc.request.getCookie("app-active").getValue)
        containerIp       <- Task(rc.request.getCookie(s"app-$currentApp").getValue)
        _                 <- logger.info(s"$currentApp proxy: ${rc.request.uri()} -> $containerIp")
        uri               =  new URI(s"http://$containerIp")
      } yield uri


    def proxyWebsocket(headers: WebSocketHeaders): Task[WSURI] =
      for {
        cookieMap         <- Task(headers.get("Cookie").split("[;,]").map(_.split("=")).map(i => i(0).trim() -> i(1).trim()).toMap)
        currentApp        =  cookieMap("app-active")
        containerIp       <- Task(cookieMap(s"app-$currentApp").split(":"))
        uri               =  WSURI(containerIp.head, containerIp(1).toInt, "/")
      } yield uri


    private def startApp(app: DesignerApp, claims: DesignerClaims, name: String, cookieName: String): Task[Cookie] =
      for {
        _                 <- logger.info(s"Starting app: $name for ${claims.firstName} ${claims.lastName}")

        client            <- kubernetes.newClient
        namespace         <- config.string("kubernetes.namespace")

        appLabel          =  LabelSelector(IsEqualRequirement("app", name))
        pod               <- kubernetes.listSelected[Pod](client, namespace, appLabel).map(_.items.headOption)
        podFiber          <- Task.when(pod.isEmpty)(
                              for {
                                volumeMount       <- UIO(Volume.Mount(name = "user-home", mountPath = "/home/harana", subPath = claims.userId))
                                containerSpec     <- UIO(Container(name = name, image = app.image, imagePullPolicy = Some(Container.PullPolicy.Always), volumeMounts = List(volumeMount)).exposePort(app.httpPort))
                                volume            <- UIO(Volume(name = "user-home", Volume.PersistentVolumeClaimRef("user-home")))

                                podSpec           <- UIO(Pod.Spec(imagePullSecrets = List(LocalObjectReference("aws-registry"))))
                                podTemplateSpec   <- UIO(Pod.Template.Spec.named(name).withPodSpec(podSpec).addContainer(containerSpec).addLabel("app" -> name).addVolume(volume))
                                deploymentSpec    <- UIO(Deployment(name).withReplicas(1).withTemplate(podTemplateSpec).withLabelSelector(appLabel))
                                _                 <- kubernetes.create(client, namespace, deploymentSpec).ignore

                                repeatSchedule    =  ((Schedule.spaced(1.seconds) && Schedule.recurWhile[ListResource[Pod]](podList =>
                                                        podList.isEmpty || !podList.head.status.get.containerStatuses.forall(_.ready)
                                                      )) >>> Schedule.elapsed).whileOutput(_ < 60.seconds)
                                _                 <- kubernetes.listSelected[Pod](client, namespace, appLabel).repeat(repeatSchedule).provide(Has(clock))
                              } yield ()
        ).fork

        existingService       <- kubernetes.get[Service](client, namespace, name)
        service               =  if (existingService.isDefined) UIO(existingService.get) else
                                  for {
                                    serviceSpec         <- UIO(Service(name, Map("app" -> name), app.httpPort))
                                    _                   <- kubernetes.create(client, namespace, serviceSpec)
                                    schedule            =  Schedule.spaced(1.seconds) && Schedule.recurWhile[Service](_.spec.get.clusterIP.isEmpty)
                                    serviceSpec         <- kubernetes.get[Service](client, namespace, name).map(_.get).repeat(schedule).map(_._2).provide(Has(clock))
                                  } yield serviceSpec
        serviceFiber          <- service.fork

        _                     <- podFiber.join
        service               <- serviceFiber.join

        containerIp           =  s"${service.spec.get.clusterIP}:${service.spec.get.ports.head.port}"
        _                     <- logger.debug(s"${app.title} is running at: $containerIp")

        cookie                <- cookie(cookieName, containerIp)

        _                     <- kubernetes.close(client)
      } yield cookie


    private def cookie(name: String, value: String) =
      for {
        domain            <- config.env("harana_domain")
        ssl               <- config.boolean("http.ssl", default = false)
        cookie            <- UIO {
                              val cookie = Cookie.cookie(name, value)
                              cookie.setDomain(domain)
                              cookie.setHttpOnly(true)
                              cookie.setPath("/")
                              cookie.setSameSite(CookieSameSite.LAX)
                              cookie.setSecure(ssl)
                            }
      } yield cookie
  }}
}