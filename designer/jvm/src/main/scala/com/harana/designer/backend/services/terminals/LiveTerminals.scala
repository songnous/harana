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
import com.harana.sdk.shared.models.terminals.{Terminal, TerminalHistory}
import com.harana.sdk.shared.utils.Random
import io.circe.jawn
import io.circe.syntax.EncoderOps
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.ext.web.RoutingContext
import skuber.json.format.podFormat
import skuber.{Container, ObjectMeta, Pod}
import zio.stream.ZStream
import zio.{Hub, Task, UIO, ZIO, ZLayer}

import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters.EnumerationHasAsScala

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
        namespace             <- config.string("terminal.namespace")
        podName               =  s"terminal-$terminalId".toLowerCase
        terminal              <- mongo.get[Terminal]("Terminals", terminalId)

        client                <- kubernetes.newClient
        _                     <- start(userId, terminalId).whenM(!kubernetes.exists(client, namespace, podName))

        sourceStream          <- subscribeToStdin(userId, terminalId)

        _                     <- kubernetes.exec(client, namespace, podName,
                                  containerName = Some("terminal"),
                                  stdin = Some(sourceStream),
                                  stdout = Some(value = (m: String) =>
                                    mongo.insert[TerminalHistory](s"Terminals-$terminalId", TerminalHistory(m)) *>
                                    vertx.publishMessage(userId, s"terminal-$terminalId-stdout", m)
                                  ),
                                  stderr = Some((m: String) =>
                                    mongo.insert[TerminalHistory](s"Terminals-$terminalId", TerminalHistory(m)) *>
                                    vertx.publishMessage(userId, s"terminal-$terminalId-stderr", m)
                                  ),
                                  command = Seq(terminal.get.shell), tty = true).retryUntilM(_ =>
                                    kubernetes.get[Pod](client, namespace, podName).map(_.isDefined).orDie
                                  ).ignore

        _                     <- kubernetes.close(client)
        response              =  Response.Empty()
      } yield response


    private def subscribeToStdin(userId: String, terminalId: String) =
      for {
        sessionId             <- UIO(Random.long)
        _                     <- vertx.publishMessage("terminal", "unsubscribe", Map("terminalId" -> terminalId, "sessionId" -> sessionId).asJson.noSpaces)
        sourceHub             <- Hub.unbounded[String]
        sourceStream          =  ZStream.fromHub(sourceHub)
        consumer              <- vertx.subscribe(userId, s"terminal-$terminalId-stdin", m =>
                                    mongo.insert[TerminalHistory](s"Terminals-$terminalId", TerminalHistory(m)) *>
                                    sourceHub.publish(m).unit
                                 )
        _                     =  subscribers.put(s"$terminalId-$sessionId-stdin", consumer)
      } yield sourceStream


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

          _                 <- kubernetes.create(client, namespace, pod)
          _                 <- kubernetes.close(client)
        } yield ()


      def disconnect(rc: RoutingContext): Task[Response] =
        for {
          client            <- kubernetes.newClient
          namespace         <- config.string("terminal.namespace")

          terminalId        <- Task(rc.request.getParam("id"))
          terminalRef       = s"terminal-$terminalId"

          _                 <- vertx.unsubscribe(subscribers.get(terminalId)).when(subscribers.contains(terminalId))
          _                 =  subscribers.remove(terminalId)
          _                 <- vertx.removeMapValue("terminal-connections", terminalId)

          _                 <- kubernetes.delete[Pod](client, namespace, terminalRef.toLowerCase)
          response          =  Response.Empty()

        } yield response


    def restart(rc: RoutingContext): Task[Response] =
      for {
        _                   <- disconnect(rc)
        _                   <- connect(rc)
        response            <- UIO(Response.Empty())
      } yield response


    def clear(rc: RoutingContext): Task[Response] =
      for {
        terminal            <- Crud.get[Terminal]("Terminals", rc, config, jwt, logger, micrometer, mongo)
        terminalId          <- Task(rc.request.getParam("id"))
        _                   <- mongo.updateFields("Terminals", terminalId, Map("history" -> List.empty)).when(terminal.isDefined)
        response            =  Response.Empty()
      } yield response


    def history(rc: RoutingContext): Task[Response] =
      for {
        terminalId          <- Task(rc.request.getParam("id"))
        messages            <- mongo.all[TerminalHistory](s"terminal-$terminalId", sort = Some(("created", true)), limit = Some(1000))
        response            =  Response.JSON(messages.asJson)
      } yield response


    def startup: Task[Unit] =
      vertx.subscribe("terminal", "unsubscribe", ids =>
        for {
          payload           <- UIO(jawn.decode[Map[String, String]](ids).getOrElse(Map()))
          oldSubscribers    =  subscribers.keys().asScala.filter(_.startsWith(payload("terminalId"))).toList
          _                 <- ZIO.foreach_(oldSubscribers)(id =>
                                  ZIO.fromCompletionStage(subscribers.get(id).unregister().toCompletionStage) *>
                                  UIO(subscribers.remove(id))
                               )
        } yield ()
      ).unit


    def shutdown: UIO[Unit] =
      for {
        _                   <- logger.error("Shutting down Terminals ..")
        terminals           = subscribers.keys().asScala.toList
        _                   <- ZIO.foreach_(terminals)(id => vertx.unsubscribe(subscribers.get(id)).ignore)
      } yield ()
  }}
}