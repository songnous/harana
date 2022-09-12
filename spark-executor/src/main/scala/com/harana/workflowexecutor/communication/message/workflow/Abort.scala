package com.harana.workflowexecutor.communication.message.workflow

import com.harana.sdk.shared.models.designer.flow.utils.Id

case class Abort(workflowId: Id)