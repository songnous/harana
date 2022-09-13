package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.actions.ConvertType
import com.harana.sdk.backend.models.flow.actions.custom.{Sink, Source}
import com.harana.sdk.shared.models.designer.flow.graph
import com.harana.sdk.shared.models.flow.actionobjects.{TargetTypeChoice, TargetTypeChoices, TypeConverterInfo}
import com.harana.sdk.shared.models.flow.graph.FlowGraph
import com.harana.sdk.shared.models.flow.graph.node.Node
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
      .setSelectedColumns(MultipleColumnSelection(Vector(NameColumnSelection(Set("column1")))))
      .parameterValuesToJson
    new ConvertType().setParametersFromJson(parameters)
  }

  private def createInnerNode(targetType: TargetTypeChoice) =
    Node(innerNodeId, createInnerNodeAction(targetType))

  def simpleGraph(targetType: TargetTypeChoice = TargetTypeChoices.StringTargetTypeChoice()): FlowGraph = {
    val innerNode = createInnerNode(targetType)
    FlowGraph(
      Set(sourceNode, sinkNode, innerNode),
      Set(graph.Edge(sourceNode, 0, innerNode, 0), graph.Edge(innerNode, 0, sinkNode, 0))
    )
  }
}