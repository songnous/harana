package com.harana.workflowexecutor

import akka.actor.{ActorContext, ActorRef}
import com.harana.sdk.backend.models.flow.CommonExecutionContext
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.designer.flow.flows.Workflow
import com.harana.sdk.shared.models.designer.flow.utils.Id

import scala.concurrent.duration.FiniteDuration

class SessionWorkflowExecutorActorProvider(
    executionContext: CommonExecutionContext,
    workflowManagerClientActor: ActorRef,
    heartbeatPublisher: ActorRef,
    notebookTopicPublisher: ActorRef,
    workflowManagerTimeout: Int,
    publisher: ActorRef,
    sessionId: String,
    heartbeatInterval: FiniteDuration
) extends Logging {

  def provide(context: ActorContext, workflowId: Id): ActorRef = {
    context.actorOf(
      SessionWorkflowExecutorActor.props(
        executionContext,
        workflowManagerClientActor,
        publisher,
        heartbeatPublisher,
        notebookTopicPublisher,
        workflowManagerTimeout,
        sessionId,
        heartbeatInterval),
      workflowId.toString
    )
  }
}