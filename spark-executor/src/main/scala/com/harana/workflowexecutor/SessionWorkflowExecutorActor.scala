package com.harana.workflowexecutor

import akka.actor.Status.Failure
import akka.actor._
import akka.pattern.{ask, pipe}
import com.harana.sdk.backend.models.flow.CommonExecutionContext
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.designer.flow.flows.WorkflowWithResults
import com.harana.workflowexecutor.WorkflowExecutorActor.Messages.Init
import com.harana.workflowexecutor.WorkflowManagerClientActorProtocol.GetWorkflow
import com.harana.workflowexecutor.communication.protocol.{Heartbeat, Ready}
import com.harana.workflowexecutor.partialexecution.Execution

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration
import scala.language.postfixOps

// Actor responsible for running workflow in an interactive way.
class SessionWorkflowExecutorActor(
    executionContext: CommonExecutionContext,
    nodeExecutorFactory: GraphNodeExecutorFactory,
    workflowManagerClientActor: ActorRef,
    publisher: ActorRef,
    heartbeatPublisher: ActorRef,
    notebookPublisher: ActorRef,
    wmTimeout: Int,
    sessionId: String,
    heartbeatInterval: FiniteDuration
) extends WorkflowExecutorActor(
      executionContext,
      nodeExecutorFactory,
      Some(workflowManagerClientActor),
      Some(publisher),
      None,
      Execution.defaultExecutionFactory
    )
    with Logging {

  import scala.concurrent.duration._

  private val heartbeat = Heartbeat(workflowId.toString, executionContext.sparkContext.uiWebUrl)
  private val scheduledHeartbeat: Option[Cancellable] = None

  override def receive: Receive = onMessage(scheduledHeartbeat)

  override def postRestart(reason: Throwable) = {
    super.postRestart(reason)
    logger.warn(s"SessionWorkflowExecutor actor for workflow: ${workflowId.toString} restarted. Re-initiating!", reason)
    initiate()
  }

  def waitingForWorkflow: Actor.Receive = {
    case Some(workflowWithResults: WorkflowWithResults) =>
      logger.debug("Received workflow with id: {}", workflowId)
      context.unbecome()
      initWithWorkflow(workflowWithResults)
    case None =>
      logger.warn("Workflow with id: {} does not exist.", workflowId)
      context.unbecome()
    case Failure(e) =>
      logger.error("Could not get workflow with id", e)
      context.unbecome()
  }

  override protected def onInitiated() = {
    scheduleHeartbeats()
    notebookPublisher ! Ready(sessionId)
  }

  private def scheduleHeartbeats() = {
    logger.info("Scheduling heartbeats.")
    scheduledHeartbeat.foreach(_.cancel())
    context.become(onMessage(Some(context.system.scheduler.scheduleAtFixedRate(Duration.Zero, heartbeatInterval, heartbeatPublisher, heartbeat))))
  }

  private def initiate() = {
    logger.debug("SessionWorkflowExecutorActor for: {} received INIT", workflowId.toString)
    workflowManagerClientActor.ask(GetWorkflow(workflowId))(wmTimeout.seconds).pipeTo(self)
    context.become(waitingForWorkflow)
  }

  private def onMessage(scheduledHeartbeat: Option[Cancellable]): Receive = {
    case Init() => initiate()
  }
}

object SessionWorkflowExecutorActor {
  def props(
      ec: CommonExecutionContext,
      workflowManagerClientActor: ActorRef,
      publisher: ActorRef,
      heartbeatPublisher: ActorRef,
      notebookPublisher: ActorRef,
      wmTimeout: Int,
      sessionId: String,
      heartbeatInterval: FiniteDuration
  ) = Props(
    new SessionWorkflowExecutorActor(ec, new GraphNodeExecutorFactoryImpl, workflowManagerClientActor, publisher, heartbeatPublisher, notebookPublisher, wmTimeout, sessionId, heartbeatInterval)
  )
}