package com.harana.designer.backend.services.flowexecutions

import com.harana.designer.backend.services.flowexecutions.FlowExecutions.Service
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.models.Response
import com.harana.sdk.shared.models.data.DataSource
import com.harana.sdk.shared.models.flow.execution.spark.{ExecutionLog, ExecutionStatus}
import com.harana.sdk.shared.models.flow.{Flow, FlowExecution}
import io.circe.parser._
import io.circe.syntax._
import io.vertx.ext.web.RoutingContext
import org.apache.commons.io.IOUtils
import zio.{Task, UIO, ZLayer}

import java.io.{File, FileInputStream}
import java.nio.charset.StandardCharsets
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object LiveFlowExecutions {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service) => new Service {

    private val logFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")
      .withLocale(Locale.ENGLISH).withZone(ZoneId.systemDefault)


    def availableFlows(rc: RoutingContext): Task[Response] =
      for {
        concurrentJobs        <- config.int("flows.concurrentJobs")
        executions            <- mongo.findEquals[FlowExecution]("FlowExecutions", Map("executionStatus" -> ExecutionStatus.PendingExecution.toString)).map(_.slice(0, concurrentJobs))
        flows                 <- Task.foreach(executions)(pe => mongo.get[Flow]("Flows", pe.flowId)).map(_.flatten)
        _                     <- logger.info(s"New flows available to execute: ${flows.map(_.title).mkString(", ")}").when(flows.nonEmpty)
        response              =  Response.JSON(executions.zip(flows).asJson)
      } yield response


    def cancelledFlows(rc: RoutingContext): Task[Response] =
      for {
        flows                 <- mongo.findEquals[Flow]("Flows", Map("executionStatus" -> ExecutionStatus.PendingCancellation.toString))
        response              =  Response.JSON(flows.asJson)
      } yield response


    def outputVariables(rc: RoutingContext): Task[Response] =
      for {
        flowId                <- Task(rc.pathParam("flowId"))
        outputVariables       <- mongo.findEquals[FlowExecution]("FlowExecutions", Map("flowId" -> flowId), Some(("updated", false))).map(_.headOption.map(_.outputVariables))
        response              =  Response.JSON(outputVariables.getOrElse(Map()).asJson)
      } yield response


    def logs(rc: RoutingContext): Task[Response] =
      for {
        logsDirectory         <- config.string("flows.logs.path")
        flowId                <- Task(rc.pathParam("flowId"))
        flowExecutionId       <- mongo.findEquals[FlowExecution]("FlowExecutions", Map("flowId" -> flowId), Some(("updated", false))).map(_.headOption.map(_.id))
        inputStream           <- Task(flowExecutionId.map(id => new FileInputStream(new File(logsDirectory, id))))
        response              <- if (inputStream.nonEmpty)
                                    for {
                                      rawLogs         <- Task(IOUtils.toString(inputStream.get, StandardCharsets.UTF_8.name()))
                                      executionLogs   <- Task.fromEither(decode[List[ExecutionLog]](rawLogs))
                                      logs            =  executionLogs.map(formatLog).mkString("\n")
                                      response        =  Response.Content(logs)
                                    } yield response
                                 else UIO(Response.Empty(statusCode = Some(404)))
      } yield response


    def progress(rc: RoutingContext): Task[Response] =
      for {
        flowId                <- Task(rc.pathParam("flowId"))
        _                     <- logger.debug(s"Getting flow execution with flow id: $flowId")
        flowExecution         <- mongo.findOne[FlowExecution]("FlowExecutions", Map("flowId" -> flowId), Some(("updated", false)))
        response              =  if (flowExecution.nonEmpty) Response.JSON(flowExecution.asJson) else Response.Empty(statusCode = Some(404))
      } yield response


    def dataSources(rc: RoutingContext): Task[Response] =
      for {
        flowId                <- Task(rc.pathParam("flowId"))
        _                     <- logger.debug(s"Getting data sources for flow id: $flowId")
        userId                <- mongo.findEquals[Flow]("Flows", Map("id" -> flowId)).map(_.head.createdBy.get)
        dataSources           <- mongo.findEquals[DataSource]("DataSources", Map("createdBy" -> userId))
        response              =  Response.JSON(dataSources.asJson)
      } yield response
  }}
}