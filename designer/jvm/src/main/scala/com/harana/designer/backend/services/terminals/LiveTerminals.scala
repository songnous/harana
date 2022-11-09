package com.harana.designer.backend.services.terminals

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.harana.designer.backend.services.Crud
import com.harana.designer.backend.services.terminals.Terminals.Service
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.kubernetes.Kubernetes
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.Vertx
import com.harana.modules.vertx.models.Response
import com.harana.sdk.shared.models.jwt.DesignerClaims
import com.harana.sdk.shared.models.terminals.Terminal
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.ext.web.RoutingContext
import skuber.json.format.podFormat
import skuber.{Container, ObjectMeta, Pod}
import zio.stream.ZStream
import zio.{Hub, Runtime, Task, UIO, ZIO, ZLayer}

import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters.ConcurrentMapHasAsScala

object LiveTerminals {

  private implicit val materializer = Materializer(ActorSystem())
  private val subscribers = new ConcurrentHashMap[String, MessageConsumer[String]]

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     jwt: JWT.Service,
                                     kubernetes: Kubernetes.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service,
                                     vertx: Vertx.Service) => new Service {

    def list(rc: RoutingContext): Task[Response] = Crud.listResponse[Terminal]("Terminals", rc, config, jwt, logger, micrometer, mongo)
    def get(rc: RoutingContext): Task[Response] = Crud.getResponse[Terminal]("Terminals", rc, config, jwt, logger, micrometer, mongo)
    def delete(rc: RoutingContext): Task[Response] = Crud.deleteResponse[Terminal]("Terminals", rc, config, jwt, logger, micrometer, mongo)
    def create(rc: RoutingContext): Task[Response] = Crud.createResponse[Terminal]("Terminals", rc, config, jwt, logger, micrometer, mongo)
    def update(rc: RoutingContext): Task[Response] = Crud.updateResponse[Terminal]("Terminals", rc, config, jwt, logger, micrometer, mongo)

    def connect(rc: RoutingContext): Task[Response] =
      for {
        userId                <- jwt.claims[DesignerClaims](rc).map(_.userId)
        terminalId            <- Task(rc.request.getParam("id"))
        existingConnection    <- vertx.getMapValue("terminal-connections", terminalId)
        _                     <- start(userId, terminalId).unless(existingConnection.nonEmpty || subscribers.asScala.contains(terminalId))
        response              =  Response.Empty()
      } yield response


    private def start(userId: String, terminalId: String): Task[Unit] =
        for {
          _                 <- logger.info(s"Starting Terminal for user: $userId")
          client            <- kubernetes.newClient

          podName           =  s"terminal-$terminalId".toLowerCase
          namespace         <- config.string("terminal.namespace")
          nodeType          <- config.string("terminal.nodeType")
          serviceAccount    <- config.string("terminal.serviceAccount")

          terminal          <- mongo.get[Terminal]("Terminals", terminalId)

          container         =  Container(
                                name = "terminal",
                                image = terminal.get.image,
                                command = List("/bin/sh" , "-c", "tail -f /dev/null"))

          podSpec           =  Pod.Spec()
                                .addContainer(container)
//                                .addNodeSelector("type" -> nodeType)
//                                .addVolume(Volume("home", Volume.PersistentVolumeClaimRef(userId)))
//                                .withServiceAccountName(serviceAccount)

          pod               =  new Pod(spec = Some(podSpec), metadata = ObjectMeta(
                                name = podName,
                                labels = Map("harana/app" -> "terminal", "harana/user" -> userId),
                                namespace = namespace))

          terminalRef       =  s"terminal-$terminalId"

          sourceHub         <- Hub.unbounded[String]
          sourceStream      =  ZStream.fromHub(sourceHub)

          consumer          <- vertx.subscribe(userId, s"$terminalRef-stdin", msg => {
                                println("STDIN: " + msg)
                                // mongo.appengit dToListField("Terminals", terminalId, "history", msg) *>
                                sourceHub.publish(msg).unit
                              })

          _                 <- UIO(subscribers.asScala.put(terminalId, consumer))

          _                 <- kubernetes.create(client, namespace, pod).whenM(!kubernetes.exists(client, namespace, podName))

          podExists         =  kubernetes.get[Pod](client, namespace, podName).map(_.isDefined).orDie

          _                 <- kubernetes.exec(client, namespace, podName,
                                containerName = Some("terminal"),
                                stdin = Some(sourceStream),
                                stdout = Some((m: String) =>
                                  logger.info("STDOUT: " + m) *>
//                                  mongo.appendToListField("Terminals", terminalId, "history", TerminalHistory(Stdout, m)) *>
                                  vertx.sendMessage(userId, s"$terminalRef-stdout", m)
                                ),
                                stderr = Some((m: String) =>
                                  logger.error("STDERR: " + m) *>
//                                  mongo.appendToListField("Terminals", terminalId, "history", TerminalHistory(Stderr, m)) *>
                                  vertx.sendMessage(userId, s"$terminalRef-stderr", m)
                                ),
                                command = Seq(terminal.get.shell),
                                tty = true).retryUntilM(_ => podExists).ignore
        } yield ()


      def disconnect(rc: RoutingContext): Task[Response] =
        for {
          client            <- kubernetes.newClient
          namespace         <- config.string("terminal.namespace")

          terminalId        <- Task(rc.request.getParam("id"))
          terminalRef       = s"terminal-$terminalId"

          _                 <- vertx.unsubscribe(subscribers.get(terminalId)).when(subscribers.contains(terminalId))
          _                 =  subscribers.remove(terminalId)

          _                 <- kubernetes.delete[Pod](client, namespace, terminalRef.toLowerCase)
          response          =  Response.Empty()

        } yield response


    def restart(rc: RoutingContext): Task[Response] =
      for {
        response            <- UIO(Response.Empty())
      } yield response


    def clear(rc: RoutingContext): Task[Response] =
      for {
        terminal            <- Crud.get[Terminal]("Terminals", rc, config, jwt, logger, micrometer, mongo)
        terminalId          <- Task(rc.request.getParam("id"))
        _                   <- mongo.updateFields("Terminals", terminalId, Map("history" -> List.empty)).when(terminal.isDefined)
        response            = Response.Empty()
      } yield response


    def history(rc: RoutingContext): Task[Response] =
      null


//    sys.addShutdownHook {
//      Runtime.default.unsafeRun {
//        for {
//          _ <- logger.info("Shutting down Terminals ..")
//          terminals = subscribers.keys().asScala.toList
//          _ <- ZIO.foreach_(terminals)(id => vertx.unsubscribe(subscribers.get(id)))
//          _ <- ZIO.foreach_(terminals)(id => vertx.removeMapValue("terminal-connections", id))
//        } yield ()
//      }
//    }
  }}
}