package com.harana.designer.backend.services

import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Background, Visibility}
import com.harana.sdk.shared.models.flow.graph.FlowGraph
import com.harana.sdk.shared.models.flow.{Flow, FlowExecution}

package object user {

  def createSampleFlow(userId: UserId): (Flow, FlowExecution) = {
    val flowGraph = FlowGraph()
    val flow = Flow("Test Flow", "This is a description about this flow", List(), flowGraph, Some(userId), Visibility.Owner, Background.RGB(0,0,0,1), Set("Flow"))
    val execution = FlowExecution(flow.id, Some(userId))
    (flow, execution)
  }

}