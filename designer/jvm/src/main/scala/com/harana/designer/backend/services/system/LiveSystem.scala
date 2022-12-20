package com.harana.designer.backend.services.system

import com.google.common.base.Charsets
import com.harana.designer.backend.services.Crud
import com.harana.designer.backend.services.system.System.Service
import com.harana.designer.shared.JavaScriptError
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.Vertx
import com.harana.modules.vertx.models.Response
import com.harana.sdk.shared.models.apps.{App => DesignerApp}
import com.harana.sdk.shared.models.common.{PendingEvent, User}
import com.harana.sdk.shared.models.data.{DataSet, DataSource}
import com.harana.sdk.shared.models.flow.{Flow, FlowExecution}
import com.harana.sdk.shared.models.schedules.{Schedule, ScheduleExecution}
import com.harana.sdk.shared.models.terminals.Terminal
import io.circe.parser._
import io.vertx.ext.web.RoutingContext
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist
import upickle.default._
import zio.clock.Clock
import zio.{Task, ZLayer}

import scala.io.Source

object LiveSystem {
  val layer = ZLayer.fromServices { (clock: Clock.Service,
                                     config: Config.Service,
                                     jwt: JWT.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service,
                                     vertx: Vertx.Service) => new Service {

    private val properties = Task {
      val airbyte = Source.fromURL(getClass.getResource("/messages_airbyte_en")).bufferedReader()
      val harana = Source.fromURL(getClass.getResource("/messages_harana_en")).bufferedReader()
      val airByteLines = LazyList.continually(airbyte.readLine()).takeWhile(_ != null).map(Jsoup.clean(_, Safelist.simpleText))
      val haranaLines = LazyList.continually(harana.readLine()).takeWhile(_ != null).map(Jsoup.clean(_, Safelist.simpleText))
      new String(java.util.Base64.getEncoder.encode(write(airByteLines.toList ++ haranaLines.toList).getBytes(Charsets.UTF_8)))
    }


    def content(rc: RoutingContext): Task[Response] =
      for {
        userId            <- Crud.userId(rc, config, jwt)
        user              <- mongo.findOne[User]("Users", Map("id" -> userId))
        id                <- Task(rc.pathParam("id"))
        response          =  if (user.isEmpty || id.contains("..") || id.contains("/")) Response.Empty(statusCode = Some(403)) else
                             Response.Template(
                               path = s"content/$id.md",
                               parameters = Map(
                                 "fileSharingUsername" -> user.get.settings.fileSharingUsername.getOrElse(""),
                                 "fileSharingPassword" -> user.get.settings.fileSharingPassword.getOrElse(""),
                                 "remoteLoginUsername" -> user.get.settings.remoteLoginUsername.getOrElse(""),
                                 "remoteLoginPassword" -> user.get.settings.remoteLoginUsername.getOrElse("")),
                               statusCode = Some(200))
      } yield response


    def health(rc: RoutingContext): Task[Response] =
      Task(Response.Empty())


    def createIndexes: Task[Unit] =
      for {
        _         <- mongo.createTextIndex[DesignerApp]("Apps", List("title", "description", "tags")).ignore
        _         <- mongo.createTextIndex[DataSource]("DataSources", List("title", "description", "tags")).ignore
        _         <- mongo.createTextIndex[DataSet]("DataSets", List("title", "description", "tags")).ignore
        _         <- mongo.createTextIndex[Flow]("Flows", List("title", "description", "tags", "actions.0.title", "actions.0.tags", "actions.0.description", "connections.0.title", "connections.0.tags", "connections.0.description")).ignore

        _         <- mongo.createIndex[DesignerApp]("Apps", Map("createdBy" -> 1)).ignore
        _         <- mongo.createIndex[DesignerApp]("Apps", Map("id" -> 1, "createdBy" -> 1), true).ignore

        _         <- mongo.createIndex[DataSet]("DataSets", Map("createdBy" -> 1)).ignore
        _         <- mongo.createIndex[DataSet]("DataSets", Map("id" -> 1, "createdBy" -> 1), true).ignore

        _         <- mongo.createIndex[DataSource]("DataSources", Map("createdBy" -> 1)).ignore
        _         <- mongo.createIndex[DataSource]("DataSources", Map("id" -> 1, "createdBy" -> 1), true).ignore
        _         <- mongo.createIndex[DataSource]("DataSources", Map("dataSourceType" -> 1, "createdBy" -> 1)).ignore

        _         <- mongo.createIndex[Flow]("Flows", Map("createdBy" -> 1)).ignore
        _         <- mongo.createIndex[Flow]("Flows", Map("id" -> 1, "createdBy" -> 1), true).ignore

        _         <- mongo.createIndex[FlowExecution]("FlowExecutions", Map("executionStatus" -> 1)).ignore
        _         <- mongo.createIndex[FlowExecution]("FlowExecutions", Map("flowId" -> 1)).ignore
        _         <- mongo.createIndex[FlowExecution]("FlowExecutions", Map("id" -> 1), true).ignore

        _         <- mongo.createIndex[Schedule]("Schedules", Map("createdBy" -> 1)).ignore
        _         <- mongo.createIndex[Schedule]("Schedules", Map("id" -> 1), true).ignore
        _         <- mongo.createIndex[Schedule]("Schedules", Map("id" -> 1, "createdBy" -> 1), true).ignore

        _         <- mongo.createIndex[Terminal]("Terminals", Map("id" -> 1, "createdBy" -> 1), true).ignore

        _         <- mongo.createIndex[User]("Users", Map("emailAddress" -> 1)).ignore
        _         <- mongo.createIndex[User]("Users", Map("id" -> 1), true).ignore
        _         <- mongo.createIndex[User]("Users", Map("id" -> 1, "subscriptionUpdated" -> 1), true).ignore
        _         <- mongo.createIndex[User]("Users", Map("subscriptionCustomerId" -> 1)).ignore
      } yield ()


    def events: Task[Unit] =
      for {
        event     <- mongo.findOneAndDelete[PendingEvent]("PendingEvents", Map(), Some(("created", true)))
        _         <- Task.when(event.isDefined)(vertx.sendMessage(event.get.address, event.get.`type`, event.get.payload))
      } yield ()


    def error(rc: RoutingContext): Task[Response] =
      for {
        errorJson     <-  Task(rc.body().asString)
        error         <-  Task(decode[JavaScriptError](errorJson))
        _             <-  logger.info(error.toOption.get.toString)
        response      =   Response.Empty()
      } yield response


    def i18n(locale: String): Task[String] =
      properties
  }}
}
