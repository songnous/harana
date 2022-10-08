package com.harana.modules.vertx


import java.net.URI
import java.util.concurrent.atomic.AtomicReference
import com.harana.modules.vertx.Vertx.{Address, Service, WebSocketHeaders}
import com.harana.modules.vertx.models._
import com.harana.modules.vertx.proxy.{WSURI, WebProxyClientOptions}
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.vertx.gc.GCHealthCheck
import io.circe.{Decoder, Encoder}
import io.circe.parser._
import io.circe.syntax._
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.{DeliveryOptions, EventBus, EventBusOptions, Message, MessageConsumer}
import io.vertx.core.http.{HttpServer, HttpServerOptions, WebSocket}
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.net.{JksOptions, NetServer, NetServerOptions}
import io.vertx.core.shareddata.{AsyncMap, Counter, Lock}
import io.vertx.core.{AsyncResult, Context, Handler, VertxOptions, Vertx => VX}
import io.vertx.ext.bridge.{BridgeOptions, PermittedOptions}
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge
import io.vertx.ext.web.client.{WebClient, WebClientOptions}
import com.harana.modules.vertx.proxy.WebProxyClient
import io.vertx.ext.web.handler.{BodyHandler, CorsHandler, SessionHandler, StaticHandler}
import io.vertx.ext.web.sstore.ClusteredSessionStore
import io.vertx.ext.web.templ.handlebars.HandlebarsTemplateEngine
import io.vertx.ext.web.{Router, RoutingContext}
import io.vertx.micrometer.{MicrometerMetricsOptions, PrometheusScrapingHandler, VertxPrometheusOptions}
import io.vertx.servicediscovery.{Record, ServiceDiscovery}
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager
import org.jose4j.jwk.JsonWebKeySet
import org.pac4j.core.client.Clients
import org.pac4j.core.config.{Config => Pac4jConfig}
import org.pac4j.core.profile.{CommonProfile, ProfileManager, UserProfile}
import org.pac4j.vertx.context.session.VertxSessionStore
import org.pac4j.vertx.handler.impl._
import org.pac4j.vertx.http.VertxHttpActionAdapter
import org.pac4j.vertx.{VertxProfileManager, VertxWebContext}
import zio.blocking._
import zio.{IO, Task, UIO, ZLayer}
import io.vertx.ext.web.sstore.cookie.CookieSessionStore

import scala.jdk.CollectionConverters._
import scala.collection.concurrent.{TrieMap, Map => ConcurrentMap}
import scala.compat.java8.FunctionConverters.asJavaFunction
import scala.compat.java8.OptionConverters._

object LiveVertx {

  System.setProperty("org.jboss.logging.provider", "log4j2")
  System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4j2LogDelegateFactory")

  LoggerFactory.initialise()

  private val vertxRef = new AtomicReference[Option[VX]](None)
  private val serviceDiscoveryRef = new AtomicReference[Option[ServiceDiscovery]](None)
  private val serviceDiscoveryListeners: ConcurrentMap[String, Record => Unit] = TrieMap.empty


  val layer = ZLayer.fromServices { (blocking: Blocking.Service,
                                     config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private def vertx =
      for {
        vertxBlockedThreads     <- config.long("vertx.blockedThreadsCheckInterval", 10000L)

        zookeeperHost           <- config.secret("zookeeper-host")
        zookeeperPrefix         <- config.optString("zookeeper.prefix")

        listenHost              <- config.string("http.listenHost")
        publicHost              <- config.string("http.publicHost", sys.env.getOrElse("POD_IP", listenHost))
        eventBusPort            <- config.int("http.eventBusPort", 10000)

        eventBusOptions         =  new EventBusOptions()
                                    .setClusterPublicHost(publicHost)
                                    .setClusterPublicPort(eventBusPort)
                                    .setLogActivity(true)

        registry                <- micrometer.registry

        clusterManager          <- UIO {
                                    val zkConfig = new JsonObject()
                                    zkConfig.put("zookeeperHosts", zookeeperHost)
                                    zkConfig.put("rootPath", zookeeperPrefix.map(p => s"$p.vertx").getOrElse("vertx"))
                                    new ZookeeperClusterManager(zkConfig)
                                   }

        vx                      <- if (vertxRef.get.isDefined) Task(vertxRef.get.get) else
                                    Task.effectAsync[VX] { cb =>
                                      VX.clusteredVertx(
                                        new VertxOptions()
                                          .setBlockedThreadCheckInterval(vertxBlockedThreads)
                                          .setClusterManager(clusterManager)
                                          .setEventBusOptions(eventBusOptions)
                                          .setMetricsOptions(new MicrometerMetricsOptions().setMicrometerRegistry(registry).setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true)).setEnabled(true)),
                                        (result: AsyncResult[VX]) => if (result.succeeded()) cb(Task.succeed(result.result)) else cb(Task.fail(result.cause()))
                                      )
                                    }
        _                       =  vertxRef.set(Some(vx))
      } yield vx


    private def serviceDiscovery: Task[ServiceDiscovery] =
      for {
        vx                      <- vertx
        serviceDiscovery        <- if (serviceDiscoveryRef.get.isDefined) Task(serviceDiscoveryRef.get.get) else Task(ServiceDiscovery.create(vx))
        _                       =  serviceDiscoveryRef.set(Some(serviceDiscovery))
      } yield serviceDiscovery


    def subscribe[T](address: Address, onMessage: (String, Option[T]) => Task[Unit])(implicit d: Decoder[T]): Task[MessageConsumer[T]] =
      for {
        eb      <- vertx.map(_.eventBus)
        result  <- IO.effectAsync[Throwable, MessageConsumer[T]] { cb =>
                    val consumer = eb.consumer(address, (message: Message[T]) => {
                      val `type` = message.headers().get("type")
                      val payload = Option(message.body()).flatMap(body => decode[T](body.asInstanceOf[String]).toOption)
                      runAsync(onMessage(`type`, payload))
                    })

                    consumer.completionHandler((result: AsyncResult[Void]) =>
                      if (result.succeeded()) cb(logger.debug(s"Subscribed to address: $address").as(consumer))
                      else cb(logger.error(s"Failed to subscribe to address: $address") *> Task.fail(result.cause()))
                    )
                  }
      } yield result


    def subscribe(address: Address, onMessage: (String, String) => Task[Unit]): Task[MessageConsumer[String]] =
      for {
        eb      <- vertx.map(_.eventBus)
        result  <- IO.effectAsync[Throwable, MessageConsumer[String]] { cb =>
                  val consumer = eb.consumer(address, (message: Message[String]) => {
                    runAsync(onMessage(message.headers().get("type"), message.body()))
                  })

                  consumer.completionHandler((result: AsyncResult[Void]) =>
                    if (result.succeeded()) cb(logger.debug(s"Subscribed to address: $address").as(consumer))
                    else cb(logger.error(s"Failed to subscribe to address: $address") *> Task.fail(result.cause()))
                  )
                }
      } yield result


    def unsubscribe(consumer: Task[MessageConsumer[_]]): Task[Unit] =
      for {
        c       <- consumer
        result  <- IO.effectAsync[Throwable, Unit] { cb =>
                    c.unregister((result: AsyncResult[Void]) =>
                      if (result.succeeded()) cb(logger.debug(s"Unsubscribed from address: ${c.address()}").unit)
                      else cb(logger.error(s"Failed to unsubscribe from address: ${c.address()}") *> Task.fail(result.cause()))
                    )
                  }
      } yield result


    def publishMessage[T](address: Address, group: String, `type`: String, payload: T)(implicit e: Encoder[T]): Task[Unit] =
      for {
        eb  <- vertx.map(_.eventBus)
        _   <- Task(eb.publish(address, payload.asJson.noSpaces, new DeliveryOptions().addHeader("type", `type`).addHeader("group", group)))
        _   <- logger.debug(s"Event bus message: ${`type`} published to address: $address")
      } yield ()


    def publishMessage(address: Address, group: String, `type`: String): Task[Unit] =
      for {
        eb  <- vertx.map(_.eventBus)
        _   <- Task(eb.send(address, null, new DeliveryOptions().addHeader("type", `type`).addHeader("group", group)))
        _   <- logger.debug(s"Event bus message: ${`type`} sent to address: $address")
      } yield ()


    def sendMessage[T](address: Address, group: String, `type`: String, payload: T)(implicit e: Encoder[T]): Task[Unit] =
      for {
        eb  <- vertx.map(_.eventBus)
        _   <- Task(eb.send(address, payload.asJson.noSpaces, new DeliveryOptions().addHeader("type", `type`).addHeader("group", group)))
        _   <- logger.debug(s"Event bus message: ${`type`} sent to address: $address")
      } yield ()


    def sendMessage(address: Address, group: String, `type`: String): Task[Unit] =
      for {
        eb  <- vertx.map(_.eventBus)
        _   <- Task(eb.send(address, null, new DeliveryOptions().addHeader("type", `type`).addHeader("group", group)))
        _   <- logger.debug(s"Event bus message: ${`type`} sent to address: $address")
      } yield ()


    def service(name: String): Task[Option[Record]] =
      for {
        sd        <- serviceDiscovery
        fn        =  (record: Record) => Boolean.box(record.getName.equals(name))
        record    <- IO.effectAsync[Throwable, Option[Record]] { cb =>
                        sd.getRecord(asJavaFunction(fn), (result: AsyncResult[Record]) =>
                          if (result.succeeded()) cb(Task.succeed(Option(result.result()))) else cb(Task.fail(result.cause()))
                        )
                      }
      } yield record


    def services(filters: Map[String, String]): Task[List[Record]] =
      for {
        sd        <- serviceDiscovery
        json      =  new JsonObject()
        _         =  filters.foreach { case (k, v) => json.put(k, v) }
        record    <- IO.effectAsync[Throwable, List[Record]] { cb =>
                        sd.getRecords(json, (result: AsyncResult[java.util.List[Record]]) =>
                          if (result.succeeded()) cb(Task.succeed(result.result().asScala.toList)) else cb(Task.fail(result.cause()))
                        )
                      }
      } yield record


    def registerServiceListener(name: String, onChange: Record => Unit): UIO[Unit] =
      UIO(serviceDiscoveryListeners.put(name, onChange))


    def deregisterServiceListener(name: String): UIO[Unit] =
      UIO(serviceDiscoveryListeners.remove(name))


    def lock(name: String): Task[Lock] =
      for {
        sharedData    <- vertx.map(_.sharedData())
        lock          <- Task.effectAsync[Lock] { cb =>
                          sharedData.getLock(name, result =>
                            if (result.succeeded()) cb(Task.succeed(result.result())) else cb(Task.fail(result.cause()))
                          )
                         }
      } yield lock


    def lockWithTimeout(name: String, timeoutSeconds: String, onLock: Lock => Task[Unit]): Task[Unit] =
      for {
        sharedData    <- vertx.map(_.sharedData())
        lock          <- Task.effectAsync[Lock] { cb =>
                            sharedData.getLock(name, result =>
                              if (result.succeeded()) cb(Task.succeed(result.result())) else cb(Task.fail(result.cause()))
                            )
                          }
      } yield ()


    def getCounter(name: String): Task[Counter] =
      for {
        sharedData    <- vertx.map(_.sharedData())
        counter       <- Task.effectAsync[Counter] { cb =>
                            sharedData.getCounter(name, (result: AsyncResult[Counter]) =>
                              if (result.succeeded()) cb(Task.succeed(result.result())) else cb(Task.fail(result.cause()))
                            )
                          }
      } yield counter


    private def withMap[K, V, X](name: String, fn: (AsyncMap[K, V], Handler[AsyncResult[X]]) => Unit): Task[X] =
      for {
        map           <- getMap[K, V](name)
        result        <- Task.effectAsync[X] { cb =>
                          fn(map, result => if (result.succeeded()) cb(Task.succeed(result.result())) else cb(Task.fail(result.cause())))
        }
      } yield result


    def clearMap[K, V](name: String): Task[Unit] =
      withMap[K, V, Void](name, (map, handler) => map.clear(handler)).unit


    def getMap[K, V](name: String): Task[AsyncMap[K, V]] =
      for {
        sharedData    <- vertx.map(_.sharedData())
        map           <- Task.effectAsync[AsyncMap[K, V]] { cb =>
                            sharedData.getAsyncMap[K, V](name, (result: AsyncResult[AsyncMap[K, V]]) =>
                              if (result.succeeded()) cb(Task.succeed(result.result())) else cb(Task.fail(result.cause()))
                            )
                          }
      } yield map


    def getMapKeys[K, V](name: String): Task[Set[K]] =
      withMap[K, V, java.util.Set[K]](name, (map, handler) => map.keys(handler)).map(_.asScala.toSet)


    def getMapValues[K, V](name: String): Task[List[V]] =
      withMap[K, V, java.util.List[V]](name, (map, handler) => map.values(handler)).map(_.asScala.toList)


    def getMapValue[K, V](name: String, key: K): Task[Option[V]] =
      withMap[K, V, V](name, (map, handler) => map.get(key, handler)).map(Option.apply)


    def putMapValue[K, V](name: String, key: K, value: V, ttl: Option[Long] = None): Task[Unit] =
      withMap[K, V, Void](name, (map, handler) => if (ttl.isDefined) map.put(key, value, ttl.get, handler) else map.put(key, value, handler)).unit


    def putMapValueIfAbsent[K, V](name: String, key: K, value: V, ttl: Option[Long] = None): Task[V] =
      withMap[K, V, V](name, (map, handler) => if (ttl.isDefined) map.putIfAbsent(key, value, ttl.get, handler) else map.putIfAbsent(key, value, handler))


    def getOrCreateContext: Task[Context] =
      vertx.map(_.getOrCreateContext())


    def getUploadedFile(filename: String): Task[Buffer] =
      vertx.map(_.fileSystem().readFileBlocking(filename))


    def close: Task[Unit] =
      vertx.map(_.close())


    def eventBus: Task[EventBus] =
      vertx.map(_.eventBus())


    def startHttpServer(domain: String,
                        proxyDomain: Option[String] = None,
                        routes: List[Route] = List(),
                        proxyMapping: Option[RoutingContext => Task[Option[URI]]] = None,
                        webSocketProxyMapping: Option[WebSocketHeaders => Task[WSURI]] = None,
                        errorHandlers: Map[Int, RoutingContext => Task[Response]] = Map(),
                        eventBusInbound: List[String] = List(),
                        eventBusOutbound: List[String] = List(),
                        authTypes: List[AuthType] = List(),
                        additionalAllowedHeaders: Set[String] = Set(),
                        postLogin: Option[(RoutingContext, Option[UserProfile]) => Task[Response]] = None,
                        sessionRegexp: Option[String] = None,
                        jwtKeySet: Option[JsonWebKeySet] = None,
                        logActivity: Boolean = false): Task[HttpServer] =
      for {
        useSSL                <- config.boolean("http.useSSL", default = false)
        publicSSL             <- config.boolean("http.publicSSL", default = true)
        listenHost            <- config.string("http.listenHost", "127.0.0.1")
        listenPort            <- config.int("http.listenPort", 8082)
        publicHost            <- config.string("http.publicHost", listenHost)
        publicPort            <- config.int("http.publicPort", if (publicSSL) 443 else 80)
        keyStorePath          <- config.optString("http.keyStorePath")
        keyStorePassword      <- config.optPassword("http.keyStorePassword")
        proxyTimeout          <- config.long("http.proxyTimeout", 24 * 60 * 60)

        publicUrl             =  if (publicSSL) s"""https://$domain${if (!publicPort.equals(443)) s":$publicPort" else ""}""" else s"""http://$domain${if (!publicPort.equals(80)) s":$publicPort" else ""}"""

        vx                    <- vertx

        clusteredStore        <- Task(CookieSessionStore.create(vx, "temp"))

        //        clusteredStore        <- Task(ClusteredSessionStore.create(vx))
        sessionStore          <- Task(new VertxSessionStore(clusteredStore))
        sessionHandler        <- Task(SessionHandler.create(clusteredStore))
        templateEngine        <- Task(HandlebarsTemplateEngine.create(vx))
        webClient             <- Task(WebClient.create(vx, new WebClientOptions().setFollowRedirects(false).setMaxRedirects(1)))
        httpClient            <- Task(vx.createHttpClient())

        router                <- Task {
                                  val router = Router.router(vx)

                                  // Common
                                  router.mountSubRouter("/eventbus", Handlers.sock(vx, eventBusInbound, eventBusOutbound))
                                  router.get("/metrics").handler(PrometheusScrapingHandler.create())
                                  router.get("/public/*").handler(setContentType(ContentType.HTML.value))
                                  router.get("/public/*").handler(StaticHandler.create("public"))
                                  router.get("/health").handler(rc => {
                                    val response = rc.response.putHeader("content-type", "text/plain")
                                    if (GCHealthCheck.current.isHealthy)
                                      response.setStatusCode(200).end("HEALTHY")
                                    else
                                      response.setStatusCode(503).end("UNHEALTHY")
                                  })
                                  router.get("/ready").handler(rc => {
                                    rc.response.putHeader("content-type", "text/plain").setStatusCode(200).end("READY")
                                  })

                                  // CORS
                                  router.route().handler(CorsHandler.create(".*.")
                                    .allowCredentials(true)
                                    .allowedHeaders((defaultAllowedHeaders ++ additionalAllowedHeaders).asJava)
                                    .allowedMethods(defaultAllowedMethods.asJava))

                                  // Body
                                  router.route.handler(BodyHandler.create())

                                  // Auth
                                  if (authTypes.nonEmpty) {
                                    val clients = authTypes.map(AuthType.getClient(vx, publicUrl, _))
                                    val authConfig = new Pac4jConfig(new Clients(publicUrl + "/callback", clients: _*))
                                    authConfig.setHttpActionAdapter(new VertxHttpActionAdapter())

                                    val callbackHandlerOptions = new CallbackHandlerOptions().setDefaultUrl("/postLogin").setMultiProfile(true)
                                    val callbackHandler = new CallbackHandler(vx, sessionStore, authConfig, callbackHandlerOptions)

                                    if (sessionRegexp.isDefined) router.routeWithRegex(sessionRegexp.get).handler(sessionHandler)
                                    router.route.handler(sessionHandler)

                                    if (jwtKeySet.nonEmpty) router.get("/jwks").handler(Handlers.jwks(jwtKeySet.get))
                                    router.get("/callback").handler(callbackHandler)
                                    router.post("/callback").handler(BodyHandler.create().setMergeFormAttributes(true))
                                    router.post("/callback").handler(callbackHandler)
                                    router.get("/login").handler(Handlers.loginForm(vx, authConfig, "public/login.hbs", Map()))
                                    router.get("/forceLogin").handler(Handlers.forceLogin(authConfig, sessionStore))
                                    router.get("/confirm").handler(Handlers.loginForm(vx, authConfig, "public/login.hbs", Map()))
                                    router.get("/logout").handler(new LogoutHandler(vx, sessionStore, new LogoutHandlerOptions(), authConfig))
                                    router.get("/centralLogout").handler(Handlers.centralLogout(vx, authConfig, sessionStore, publicUrl))
                                    router.get("/postLogin").handler(rc => {
                                      val profileManager = new VertxProfileManager(new VertxWebContext(rc, sessionStore), sessionStore)
                                      val postLoginHandler = postLogin.get.apply(_, profileManager.getProfile.asScala)
                                      generateResponse(vx, micrometer, templateEngine, rc, postLoginHandler, auth = false)
                                    })
                                  }

                                  // Custom Routes
                                  routes.foreach { route =>
                                    def handler(rc: RoutingContext): Unit =
                                      generateResponse(vx, micrometer, templateEngine, rc, route.handler, route.isSecured)

                                    if (route.isRegex) {
                                      if (route.isBlocking)
                                        router.routeWithRegex(route.method, route.path).virtualHost(domain).blockingHandler(handler)
                                      else
                                        router.routeWithRegex(route.method, route.path).virtualHost(domain).handler(handler)
                                    }
                                    else {
                                      val customRoute =
                                        if (route.isBlocking)
                                          router.route(route.method, route.path).virtualHost(domain).blockingHandler(handler).useNormalizedPath(route.isNormalisedPath)
                                        else
                                          router.route(route.method, route.path).virtualHost(domain).handler(handler).useNormalizedPath(route.isNormalisedPath)

                                      if (route.consumes.isDefined) customRoute.consumes(route.consumes.get.value)
                                      if (route.produces.isDefined) customRoute.produces(route.produces.get.value)
                                    }
                                  }

                                  // Proxy
                                  println(s"Proxy domain = ${proxyDomain.isDefined}, Proxy mapping = ${proxyMapping.isDefined}")
                                  if (proxyDomain.isDefined && proxyMapping.isDefined) {
                                    val client = new WebProxyClient(webClient, WebProxyClientOptions(iFrameAncestors = List(domain, proxyDomain.get)))
                                    println(s"Creating virtual host for: ${proxyDomain.get}")
                                    router.route().virtualHost(proxyDomain.get).blockingHandler(rc => {
                                      println(s"Proxing for: ${rc.request.uri()}")
                                      run(proxyMapping.get(rc)) match {
                                        case Some(uri) => client.execute(rc, "/*", uri)
                                        case None => rc.response.end()
                                      }
                                    })
                                  }

                                  // Errors
                                  router.route.failureHandler((rc: RoutingContext) => {
                                    val response = rc.response
                                    errorHandlers.get(response.getStatusCode) match {
                                      case Some(r) => generateResponse(vx, micrometer, templateEngine, rc, r, auth = false)
                                      case None => if (!response.closed() && !response.ended()) response.end()
                                    }
                                  })

                                  router
                                }

      options                 <- UIO {
                                  var httpServerOptions = new HttpServerOptions()
                                    .setCompressionSupported(true)
                                    .setDecompressionSupported(true)
                                    .setLogActivity(logActivity)
                                    .setHost(listenHost)
                                    .setMaxHeaderSize(1024 * 16)
                                    .setPort(listenPort)
                                    .setSsl(useSSL)
                                    .setUseAlpn(getVersion >= 9)

                                    if (keyStorePath.isDefined) httpServerOptions = httpServerOptions.setKeyStoreOptions(
                                      new JksOptions().setPath(keyStorePath.get).setPassword(keyStorePassword.get)
                                    )

                                  httpServerOptions
                                }

        httpServer            <- blocking.blocking(IO.effectAsync[Throwable, HttpServer] { cb =>
                                  vx.createHttpServer(options)
                                    .requestHandler(router)
                                    .webSocketHandler(sourceSocket => {
                                      if (webSocketProxyMapping.isDefined && !sourceSocket.uri().startsWith("/eventbus")) {
                                        val target = run(webSocketProxyMapping.get(sourceSocket.headers()))

                                        logger.info(s"Syncing socket: ${target.host}:${target.port} -> ${sourceSocket.uri()}")
                                        httpClient.webSocket(target.port, target.host, sourceSocket.uri(), (connection: AsyncResult[WebSocket]) => {
                                          if (connection.succeeded()) {
                                            logger.info(s"Connection succeeded for: ${target.host}:${target.port} -> ${sourceSocket.uri()}")
                                            val targetSocket = connection.result()
                                            syncSockets(sourceSocket, targetSocket)
                                          } else {
                                            logger.warn(s"Failed to connect to backend WS: $target")
                                          }
                                        })
                                      }
                                    })
                                    .listen(listenPort, listenHost, (result: AsyncResult[HttpServer]) =>
                                      if (result.succeeded()) {
                                        cb(
                                          (
                                            logger.info(s"Started HTTP server on $listenHost:$listenPort") *>
                                            logger.info(s"Routes: ${router.getRoutes.asScala.map(_.getPath).mkString(", ")}")).as(result.result())
                                        )
                                      } else {
                                        cb(logger.error(s"Failed to start HTTP server on $listenHost:$listenPort") *> Task.fail(result.cause()))
                                      }
                                    )
                                  })

    } yield httpServer


    def startNetServer(listenHost: String, listenPort: Int, options: Option[NetServerOptions] = None): Task[NetServer] =
      for {
        vx      <- vertx
        result  <- IO.effectAsync[Throwable, NetServer] { cb =>
                    vx.createNetServer().listen(listenPort, listenHost, (result: AsyncResult[NetServer]) =>
                      if (result.succeeded()) cb(Task(result.result())) else cb(Task.fail(result.cause())))
                    }
      } yield result


    def startTcpEventBusServer(listenHost: String, listenPort: Int, inAddressRegex: String, outAddressRegex: String): Task[Unit] =
      for {
        vx      <- vertx
        result  <- IO.effectAsync[Throwable, Unit] { cb =>
                    TcpEventBusBridge.create(vx, new BridgeOptions()
                      .addInboundPermitted(new PermittedOptions().setAddressRegex(inAddressRegex))
                      .addOutboundPermitted(new PermittedOptions().setAddressRegex(outAddressRegex)))
                      .listen(listenPort, listenHost, (result: AsyncResult[TcpEventBusBridge]) =>
                        if (result.succeeded()) cb(Task.succeed(result.result())) else cb(Task.fail(result.cause())))
                  }
      } yield result
  }}
}