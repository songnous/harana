package com.harana.executor.spark.modules

import com.harana.executor.spark.modules.spark.Spark.Service
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.Vertx
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.sdk.shared.models.designer.data.DataSource
import com.harana.sdk.shared.models.flow.FlowExecution.FlowExecutionId
import com.harana.sdk.backend.models.flow.{Flow, FlowExecution}
import com.harana.executor.spark.metrics.MetricsManager
import com.harana.sdk.shared.models.designer.flow.ActionInfo
import io.circe.syntax._
import org.apache.spark.sql.SparkSession
import zio.{Task, UIO}

import java.nio.file.{Files, NoSuchFileException, Paths, StandardOpenOption}

package object flowexecutor {

  def logActions(logger: Logger.Service, actions: List[Action]): Task[Unit] =
    for {
      _ <- logger.trace("")
      _ <- logger.trace("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
      _ <- logger.trace(s" ${actions.map(_.title.getOrElse("")).mkString(", ")}")
      _ <- logger.trace("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
      _ <- logger.trace("")
    } yield ()


  def sparkSession(config: Config.Service, spark: Service, flow: Flow, flowExecution: FlowExecution): Task[SparkSession] =
    for {
      sparkMaster       <- config.string("spark.master")
      config            =  Map(
                              "spark.master" -> sparkMaster,
                              "spark.driver.memory" -> s"${flowExecution.maximumExecutorMemory.get.toString}g",
                              "spark.executor.memory" -> s"${flowExecution.maximumExecutorMemory.get.toString}g",
                              "spark.dynamicAllocation.enabled" -> "true",
                              "spark.dynamicAllocation.maxExecutors" -> flowExecution.executorCount.toString,
                              "spark.extraListeners" -> "com.harana.spark.metrics.Listener",
                              "harana.flowExecutionId" -> flowExecution.id
                            )
      sparkSession      <- spark.newSession(flow.title, config)
     } yield sparkSession


  def dataSources(mongo: Mongo.Service, flow: Flow): Task[List[DataSource]] =
    for {
      userId                   <- mongo.findEquals[Flow]("Flows", Map("id" -> flow.id)).map(_.head.createdBy.get)
      dataSources              <- mongo.findEquals[DataSource]("DataSources", Map("createdBy" -> userId))
    } yield dataSources


  def updateProgress(config: Config.Service, vertx: Vertx.Service, flowExecutionId: FlowExecutionId): Task[Unit] =
    for {
      logPath                   <- config.string("logs.path").map(dir => Paths.get(dir, flowExecutionId))

      (flowExecution, logs)     <- UIO(MetricsManager.getFlowExecutionAndLogs(flowExecutionId))
      logsJson                  =  logs.asJson.noSpaces.getBytes()
      
      isDirty                   <- UIO(MetricsManager.isDirty(flowExecutionId))
      _                         <- Task.when(isDirty)(Task(Files.write(logPath, logsJson, StandardOpenOption.TRUNCATE_EXISTING)).catchSome { case _: NoSuchFileException => Task(Files.write(logPath, logsJson, StandardOpenOption.CREATE)) })
      _                         <- Task.when(isDirty)(vertx.publishMessage(flowExecution.createdBy.get, "designer", "flow.execution.updated", Some(flowExecution)))
      _                         <- Task.when(isDirty)(UIO(MetricsManager.clearDirty(flowExecutionId)))
    } yield ()
}