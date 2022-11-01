package com.harana.designer.backend.services.terminals

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
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
import com.harana.sdk.shared.models.terminals.HistoryType.{Stderr, Stdout}
import com.harana.sdk.shared.models.terminals.{Terminal, TerminalHistory}
import io.circe.syntax.EncoderOps
import io.vertx.core.Handler
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.ext.web.RoutingContext
import skuber.json.format.podFormat
import skuber.{Container, ObjectMeta, Pod, Volume}
import zio.stream.ZStream
import zio.{Chunk, Task, ZIO, ZLayer}

import java.util.concurrent.atomic.AtomicReference

object LiveTerminals {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     jwt: JWT.Service,
                                     kubernetes: Kubernetes.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service,
                                     vertx: Vertx.Service) => new Service {

    implicit val materializer = Materializer(ActorSystem())

    def list(rc: RoutingContext): Task[Response] = Crud.listResponse[Terminal]("Terminals", rc, config, jwt, logger, micrometer, mongo)
    def get(rc: RoutingContext): Task[Response] = Crud.getResponse[Terminal]("Terminals", rc, config, jwt, logger, micrometer, mongo)
    def delete(rc: RoutingContext): Task[Response] = Crud.deleteResponse[Terminal]("Terminals", rc, config, jwt, logger, micrometer, mongo)
    def create(rc: RoutingContext): Task[Response] = Crud.createResponse[Terminal]("Terminals", rc, config, jwt, logger, micrometer, mongo)
    def update(rc: RoutingContext): Task[Response] = Crud.updateResponse[Terminal]("Terminals", rc, config, jwt, logger, micrometer, mongo)

    def start(rc: RoutingContext): Task[Response] =
        for {
          client            <- kubernetes.newClient

          userId            <- jwt.claims[DesignerClaims](rc).map(_.userId)
          terminalId        <- Task(rc.request.getParam("id"))

          podName           =  s"terminal-$userId-$terminalId"
          namespace         <- config.string("terminal.namespace")
          image             <- config.string("terminal.image")
          nodeType          <- config.string("terminal.nodeType")
          serviceAccount    <- config.string("terminal.serviceAccount")

          container         =  Container(name = "terminal", image = image)
          podSpec           =  Pod.Spec()
                                .addContainer(container)
                                .addNodeSelector("type" -> nodeType)
                                .addVolume(Volume("home", Volume.PersistentVolumeClaimRef(userId)))
                                .withServiceAccountName(serviceAccount)

          pod               =  new Pod(spec = Some(podSpec), metadata = ObjectMeta(
                                name = podName,
                                labels = Map("harana/app" -> "terminal", "harana/user" -> userId),
                                namespace = namespace))

          terminalRef       =  s"terminal-$terminalId"

          clientConsumer    <- vertx.subscribe[String](userId, (messageType, message) =>
                                if (messageType.equals(terminalRef))
                                  mongo.appendToListField("Terminals", terminalId, "history", message)
                               )

          stream            =  ZStream.effectAsync[Any, Nothing, String] { cb => clientConsumer.handler { event =>
                                  if (event.headers().get("type").equals(terminalRef))
                                    cb(ZIO.succeed(Chunk(event.body())))
                               }}

          _                 <- kubernetes.create(client, namespace, pod)
          _                 <- kubernetes.exec(client, namespace, podName,
                                maybeStdin = Some(stream),
                                maybeStdout = Some((m: String) =>
                                  (mongo.appendToListField("Terminals", terminalId, "history", TerminalHistory(Stdout, m)) *>
                                   vertx.sendMessage(userId, s"$terminalRef-stdout", m)).ignore
                                ),
                                maybeStderr = Some((m: String) =>
                                  (mongo.appendToListField("Terminals", terminalId, "history", TerminalHistory(Stderr, m)) *>
                                   vertx.sendMessage(userId, s"$terminalRef-stderr", m)).ignore
                                ),
                                command = Seq("bash"),
                                tty = true)

            _               <- kubernetes.close(client)

          response          =  Response.Empty()
        } yield response


      def stop(rc: RoutingContext) =
        for {
          client            <- kubernetes.newClient
          namespace         <- config.string("terminal.namespace")

          userId            <- jwt.claims[DesignerClaims](rc).map(_.userId)
          terminalId        <- Task(rc.request.getParam("id"))
          podName           = s"terminal-$userId-$terminalId"

          _                 <- vertx.unsubscribe(s"terminal-$terminalId")

          _                 <- kubernetes.delete[Pod](client, namespace, podName)
          response          =  Response.Empty()

        } yield response


    def restart(rc: RoutingContext): Task[Response] =
      for {
        _                   <- stop(rc)
        _                   <- start(rc)
        response            = Response.Empty()
      } yield response


    def clear(rc: RoutingContext): Task[Response] =
      for {
        terminal            <- Crud.get[Terminal]("Terminals", rc, config, jwt, logger, micrometer, mongo)
        terminalId          <- Task(rc.request.getParam("id"))
        _                   <- mongo.updateFields("Terminals", terminalId, Map("history" -> List.empty)).when(terminal.isDefined)
        response            = Response.Empty()
      } yield response


    def isTerminalRunning(rc: RoutingContext): Task[Response] =
      for {
        client              <- kubernetes.newClient
        namespace           <- config.string("terminal.namespace")
        userId              <- jwt.claims[DesignerClaims](rc).map(_.userId)
        podName             =  s"terminal-$userId"
        exists              <- kubernetes.get(client, namespace, podName).map(_.isDefined)
        _                   <- kubernetes.close(client)
        response            =  Response.JSON(exists.asJson)
      } yield response
  }}
}