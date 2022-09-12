package com.harana.workflowexecutor.communication.message.workflowmanager

import com.harana.sdk.shared.models.designer.flow.flows.Workflow
import com.harana.sdk.shared.models.designer.flow.utils.Id

object WorkflowManagerClientProtocol {
  case class GetWorkflow(workflowId: Id)
}
