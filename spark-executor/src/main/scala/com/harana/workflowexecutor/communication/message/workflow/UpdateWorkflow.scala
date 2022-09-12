package com.harana.workflowexecutor.communication.message.workflow

import com.harana.sdk.shared.models.designer.flow.utils.Id
import com.harana.sdk.shared.models.designer.flow.flows.Workflow

case class UpdateWorkflow(workflowId: Id, workflow: Workflow)