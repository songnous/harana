package com.harana.workflowexecutor

import akka.actor._
import com.harana.sdk.backend.models.designer.flow.CommonExecutionContext
import com.harana.sdk.backend.models.designer.flow.utils.Logging
import com.harana.sdk.shared.models.designer.flow.flows.WorkflowWithResults
import com.harana.workflowexecutor.partialexecution.Execution

class BatchWorkflowExecutorActor(executionContext: CommonExecutionContext, nodeExecutorFactory: GraphNodeExecutorFactory, terminationListener: ActorRef, initWorkflow: WorkflowWithResults) extends WorkflowExecutorActor(
      executionContext,
      nodeExecutorFactory,
      None,
      None,
      Some(terminationListener),
      Execution.defaultExecutionFactory
    )
    with Actor
    with Logging {

  initWithWorkflow(initWorkflow)

  override def receive: Actor.Receive = ready()

}

object BatchWorkflowExecutorActor {

  def props(ec: CommonExecutionContext, statusListener: ActorRef, initWorkflow: WorkflowWithResults) =
    Props(new BatchWorkflowExecutorActor(ec, new GraphNodeExecutorFactoryImpl, statusListener, initWorkflow))

}
