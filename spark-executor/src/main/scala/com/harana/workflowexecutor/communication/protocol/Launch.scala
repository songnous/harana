package com.harana.workflowexecutor.communication.protocol

import com.harana.sdk.shared.models.designer.flow.graph.node.Node
import com.harana.sdk.shared.models.designer.flow.utils.Id

case class Launch(workflowId: Id, nodesToExecute: Set[Node.Id])