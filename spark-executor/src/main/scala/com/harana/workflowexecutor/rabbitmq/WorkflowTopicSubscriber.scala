package com.harana.workflowexecutor.rabbitmq

import akka.actor._
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.designer.flow.flows.Workflow
import com.harana.sdk.shared.models.designer.flow.utils.Id
import com.harana.spark.AkkaUtils
import com.harana.workflowexecutor.{SessionWorkflowExecutorActorProvider, WorkflowExecutorActor}
import com.harana.workflowexecutor.communication.message.workflow
import com.harana.workflowexecutor.communication.protocol.{Launch, PoisonPill}
import com.harana.workflowexecutor.executor.Executor

// Handles messages with topic workflow.&#36;{id}. All messages directed to workflows.
case class WorkflowTopicSubscriber(actorProvider: SessionWorkflowExecutorActorProvider, sessionId: String, workflowId: Id) extends Actor with Logging with Executor {

  private val executorActor: ActorRef = actorProvider.provide(context, workflowId)

  override def receive: Receive = {
    case WorkflowExecutorActor.Messages.Init() =>
      logger.debug(s"Initializing SessionWorkflowExecutorActor for workflow '$workflowId'")
      executorActor ! WorkflowExecutorActor.Messages.Init()

    case Launch(id, nodesToExecute) if id == workflowId =>
      logger.debug(s"LAUNCH! '$workflowId'")
      executorActor ! WorkflowExecutorActor.Messages.Launch(nodesToExecute)

    case workflow.Abort(id) if id == workflowId =>
      logger.debug(s"ABORT! '$workflowId'")
      executorActor ! WorkflowExecutorActor.Messages.Abort()

    case workflow.UpdateWorkflow(id, workflow) if id == workflowId =>
      logger.debug(s"UPDATE STRUCT '$workflowId'")
      executorActor ! WorkflowExecutorActor.Messages.UpdateStruct(workflow)

    case workflow.Synchronize() =>
      logger.debug(s"Got Synchronize() request for workflow '$workflowId'")
      executorActor ! WorkflowExecutorActor.Messages.Synchronize()

    case PoisonPill() =>
      logger.info("Got PoisonPill! Terminating Actor System!")
      AkkaUtils.terminate(context.system)

    case x =>
      logger.error(s"Unexpected '$x' from '${sender()}'!")
  }

}

object WorkflowTopicSubscriber {

  def props(actorProvider: SessionWorkflowExecutorActorProvider, sessionId: String, workflowId: Id): Props = {
    Props(WorkflowTopicSubscriber(actorProvider, sessionId, workflowId))
  }

}
