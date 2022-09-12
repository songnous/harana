package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.flow.utils.Id

sealed trait WorkflowQueueTopic

object WorkflowQueueTopic {
  case class Start(workflowId: Id, nodesToExecute: Set[Id]) extends WorkflowQueueTopic
  case class Stop(workflowId: Id) extends WorkflowQueueTopic
  case class Update(workflowId: Id, workflow: Flow) extends WorkflowQueueTopic
  case class Ready(sessionId: String) extends WorkflowQueueTopic
  case class Heartbeat(workflowId: Id) extends WorkflowQueueTopic
  case class State() extends WorkflowQueueTopic
  case class RequestStateUpdate() extends WorkflowQueueTopic
  case class InferredState() extends WorkflowQueueTopic
}