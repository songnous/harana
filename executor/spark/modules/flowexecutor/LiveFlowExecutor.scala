package com.harana.executor.spark.modules.flowexecutor

import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.Vertx
import com.harana.sdk.backend.models.flow.ActionType._
import com.harana.sdk.backend.models.flow._
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.flow.Action.ActionId
import com.harana.sdk.shared.models.flow.FlowExecution.FlowExecutionId
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.execution.ExecutionStatus
import com.harana.executor.spark.actiontypes.ActionTypes
import com.harana.executor.spark.{everySecond, logDebug}
import com.harana.executor.spark.metrics.MetricsManager
import com.harana.executor.spark.modules.flowexecutor.FlowExecutor.Service
import com.harana.executor.spark.modules.spark.Spark
import com.harana.sdk.shared.models.designer.flow.ActionInfo
import org.apache.logging.log4j.ThreadContext
import zio._
import zio.clock.Clock

import java.util.concurrent.atomic.AtomicInteger

object LiveFlowExecutor {
  val layer = ZLayer.fromServices { (clock: Clock.Service,
                                     config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service,
                                     spark: Spark.Service,
                                     vertx: Vertx.Service) => new Service {

    def executeFlows: Task[Unit] =
      for {
        _                     <- logDebug(s"Checking for available flows.")
        concurrency           <- config.int("flows.concurrency")

        executions            <- mongo.findEquals[FlowExecution]("FlowExecutions", Map("executionStatus" -> ExecutionStatus.PendingExecution.toString)).map(_.slice(0, concurrency))
        flows                 <- Task.foreach(executions)(pe => mongo.get[Flow]("Flows", pe.flowId)).map(_.flatten)
        _                     <- logger.info(s"New flows available to execute: ${flows.map(_.title).mkString(", ")}").when(flows.nonEmpty)

        flowPairs             =  executions.zip(flows)
        _                     <- ZIO.foreachPar_(flowPairs)(fp => executeFlow(fp._2, fp._1).ignore)
      } yield ()


    def executeFlow(flow: Flow, flowExecution: FlowExecution): Task[Unit] =
      for {
        _                             <- UIO(ThreadContext.put("flowExecutionId", flowExecution.id))
        _                             <- UIO(MetricsManager.startFlow(flow, flowExecution))
        _                             <- updateProgress(config, vertx, flowExecution.id)

        sparkSession                  <- sparkSession(config, spark, flow, flowExecution)
        dataSources                   <- dataSources(mongo, flow)
        haranaFilesPath               <- config.string("files.harana")
        temporaryPath                 <- config.string("files.temporary")

        context                       =  FlowContext(sparkSession, sparkSession.sparkContext, dataSources.map { ds => ds.id -> ds }.toMap, flowExecution.createdBy.get, haranaFilesPath, temporaryPath)

        completedActionsRef           <- Ref.make(Map[ActionId, Option[Outputs]]())
        completedActionsCount         <- IO.effectTotal(new AtomicInteger(0))
        _                             <- executeAvailableActions(flow, flowExecution, context, completedActionsRef, completedActionsCount).repeatWhile(_ => completedActionsCount.intValue() < flow.actions.size)

        _                             <- Task(sparkSession.stop())
        _                             <- UIO(ThreadContext.clearAll())
        _                             <- UIO(MetricsManager.stopFlow(flowExecution.id))
        _                             <- updateProgress(config, vertx, flowExecution.id)
        _                             <- UIO(MetricsManager.clearFlow(flowExecution.id))
      } yield ()

    // Execute all actions that either (a) have not been completed or (b) have all input link actions completed.
    private def executeAvailableActions(flow: Flow,
                                        flowExecution: FlowExecution,
                                        context: FlowContext,
                                        completedActionsRef: Ref[Map[ActionId, Option[Outputs]]],
                                        completedActionsCount: AtomicInteger) =
      for {
        completedActions              <- completedActionsRef.get
        nextActions                   =  flow.actions.filter(a => !completedActions.contains(a.id) && flow.links.filter(_.toAction == a.id).map(_.fromAction).forall(completedActions contains))
        _                             <- logActions(logger, nextActions)
        actionResults                 <- ZIO.foreachPar(nextActions)(ra => executeAction(flowExecution, ra, context, flow.links.filter(_.toAction == ra.id), completedActions, completedActionsCount))
        _                             <- completedActionsRef.set(completedActions ++ actionResults)
      } yield actionResults


    private def executeAction(flowExecution: FlowExecution,
                              action: Action,
                              context: FlowContext,
                              inputLinks: List[Link],
                              completedActions: Map[ActionId, Option[Outputs]],
                              completedActionsCount: AtomicInteger): Task[(ActionId, Option[Outputs])] =
      for {
        progressTimer                 <- updateProgress(config, vertx, flowExecution.id).repeat(everySecond).provide(Has(clock)).fork

        actionInputs                  <- Task(new Inputs(inputLinks.map(l => (l.toPort, completedActions(l.fromAction).get.get[Port, Port#A](l.fromPort).get)).toMap)).onError(e => failedAction(flowExecution.id, action, e, progressTimer).ignore)
        actionType                    =  ActionTypes.get(action.actionType)
        actionParameters              =  actionType.parameterGroups.flatMap(_.parameters)

        actionParameterValues         <- Task(actionParameters.map { ap => (ap, action.parameterValues.get(ap.name))}.collect { case (k, Some(v)) => k -> v }).onError(e => failedAction(flowExecution.id, action, e, progressTimer).ignore)
        actionParameterValuesHMap     =  new ParameterValues(actionParameterValues.toMap)

        _                             <- UIO(MetricsManager.startAction(flowExecution.id, action.id))
        actionOutputs                 <- actionType.execute(actionInputs, actionParameterValuesHMap, context).mapError(error => new Exception(error.toString)).onError(e => failedAction(flowExecution.id, action, e, progressTimer).ignore)
        _                             <- UIO(MetricsManager.stopAction(flowExecution.id, action.id))

        _                             <- UIO(completedActionsCount.incrementAndGet())
        _                             <- progressTimer.interrupt
      } yield {
        (action.id, actionOutputs)
      }


      private def failedAction(flowExecutionId: FlowExecutionId, action: Action, error: zio.Cause[Throwable], progressTimer: Fiber[Throwable, Long]): Task[Unit] =
        for {
          _                 <- logger.error(error.squash.getMessage)
          _                 =  MetricsManager.failAction(flowExecutionId, action.id, error.squash.getMessage)
          _                 <- updateProgress(config, vertx, flowExecutionId)
          _                 =  MetricsManager.clearFlow(flowExecutionId)
          _                 <- progressTimer.interrupt
        } yield ()
    }
  }
}