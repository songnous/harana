package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.actiontypes.ConvertType
import com.harana.sdk.backend.models.flow.actiontypes.custom.{Sink, Source}
import com.harana.sdk.shared.models.flow.actionobjects.{TargetTypeChoice, TargetTypeChoices, TypeConverterInfo}
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.graph.{Edge, FlowGraph}
import com.harana.sdk.shared.models.flow.parameters.selections.{MultipleColumnSelection, NameColumnSelection}

object InnerWorkflowTestFactory {

  val sourceNodeId = "2603a7b5-aaa9-40ad-9598-23f234ec5c32"
  val sinkNodeId = "d7798d5e-b1c6-4027-873e-a6d653957418"
  val innerNodeId = "b22bd79e-337d-4223-b9ee-84c2526a1b75"

  val sourceNode = Node(sourceNodeId, new Source())
  val sinkNode = Node(sinkNodeId, new Sink())

  private def createInnerNodeAction(targetType: TargetTypeChoice) = {
    val parameters = TypeConverterInfo
      .setTargetType(targetType)
      .setSelectedColumns(MultipleColumnSelection(List(NameColumnSelection(Set("column1")))))
      .parameterValuesToJson
    new ConvertType().setParametersFromJson(parameters)
  }

  private def createInnerNode(targetType: TargetTypeChoice) =
    Node(innerNodeId, createInnerNodeAction(targetType))

  def simpleGraph(targetType: TargetTypeChoice = TargetTypeChoices.StringTargetTypeChoice()): FlowGraph = {
    val innerNode = createInnerNode(targetType)
    FlowGraph(
      Set(sourceNode, sinkNode, innerNode),
      Set(Edge((sourceNode.id, 0), (innerNode.id, 0)), Edge((innerNode.id, 0), (sinkNode.id, 0)))
    )
  }
}