package com.harana.sdk.backend.models.flow.workflows

import com.harana.sdk.backend.models.flow.graph.GraphKnowledge
import com.harana.sdk.shared.models.flow.ExecutionReport
import com.harana.sdk.shared.models.flow.utils.Id

case class InferredState(id: Id,
                         graphKnowledge: GraphKnowledge,
                         states: ExecutionReport)
