package com.harana.designer.backend.services.terminal

import akka.stream.Materializer
import akka.actor.ActorSystem
import com.harana.designer.backend.services.terminal.Terminal.Service
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.id.jwt.shared.models.DesignerClaims
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.kubernetes.Kubernetes
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.Vertx
import com.harana.modules.vertx.models.Response
import io.circe.syntax.EncoderOps
import io.vertx.ext.web.RoutingContext
import skuber.json.format.podFormat
import skuber.{Container, ObjectMeta, Pod, Volume}
import zio.{Task, UIO, ZIO, ZLayer}

object LiveTerminal {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     jwt: JWT.Service,
                                     kubernetes: Kubernetes.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service,
                                     vertx: Vertx.Service) => new Service {

    implicit val materializer = Materializer(ActorSystem())

    def startTerminal(rc: RoutingContext): Task[Response] =
        for {
          client            <- kubernetes.newClient

          userId            <- jwt.claims[DesignerClaims](rc).map(_.userId)

          podName           =  s"terminal-$userId"
          namespace         <- config.string("terminal.namespace")
          image             <- config.string("terminal.image")
          nodeType          <- config.string("terminal.nodeType")
          serviceAccount    <- config.string("terminal.serviceAccount")

          container         =  Container(name = "terminal", image = image)
          podSpec           =  Pod.Spec()
                                .addContainer(container)
                                .addImagePullSecretRef("mySecret")
                                .addNodeSelector("type" -> nodeType)
                                .addVolume(Volume("home", Volume.PersistentVolumeClaimRef(userId)))
                                .withServiceAccountName(serviceAccount)

          pod               =  new Pod(spec = Some(podSpec), metadata = ObjectMeta(
                                name = podName,
                                labels = Map("harana/app" -> "terminal", "harana/user" -> userId),
                                namespace = namespace))

          _                 <- kubernetes.create(client, namespace, pod)
          logs              <- kubernetes.getPodLogSource(client, namespace, podName, Pod.LogQueryParams(containerName = Some("terminal"), sinceTime = pod.metadata.creationTimestamp))
          _                 <- ZIO.fromFuture(_ => logs.runForeach(line => vertx.sendMessage("harana-terminal", userId, line.utf8String)))
          _                 <- kubernetes.close(client)

          response          =  Response.Empty()
        } yield response


      def stopTerminal(rc: RoutingContext) =
        null


      def restartTerminal(rc: RoutingContext): Task[Response] =
        for {
          response          <-  UIO(Response.Empty())
        } yield response


      def isTerminalRunning(rc: RoutingContext): Task[Response] =
        for {
          client            <- kubernetes.newClient
          namespace         <- config.string("terminal.namespace")
          userId            <- jwt.claims[DesignerClaims](rc).map(_.userId)
          podName           =  s"terminal-$userId"
          exists            <- kubernetes.get(client, namespace, podName).map(_.isDefined)
          _                 <- kubernetes.close(client)
          response          =  Response.JSON(exists.asJson)
        } yield response
  }}
}