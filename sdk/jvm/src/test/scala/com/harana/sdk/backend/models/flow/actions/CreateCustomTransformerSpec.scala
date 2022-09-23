package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects.{CustomTransformer, TypeConverter}
import com.harana.sdk.backend.models.flow.actions.custom.{Sink, Source}
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.CustomTransformer
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.shared.models.designer.flow.parameters.custom
import com.harana.sdk.shared.models.flow.actionobjects.{TargetTypeChoices, TypeConverterInfo}
import com.harana.sdk.shared.models.flow.graph.{Edge, FlowGraph}
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.parameters.ParameterType
import com.harana.sdk.shared.models.flow.parameters.custom.{InnerWorkflow, PublicParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.{MultipleColumnSelection, NameColumnSelection}
import io.circe.Json

class CreateCustomTransformerSpec extends UnitSpec {

  val node1Id = Node.Id.randomId
  val node2Id = Node.Id.randomId

  object MockCreateCustomTransformer extends CreateCustomTransformer {

    def createWithParameter = {
      set(
        innerWorkflowParameter,
        createInnerWorkflow(
          PublicParameter(node1Id, "target type", "public param 1"),
          PublicParameter(node2Id, "target type", "public param 2")
        )
      )
    }
  }

  "CreateCustomTransformer" should {

    "create CustomTransformer with public parameters" in {

      val action = MockCreateCustomTransformer.createWithParameter
      val executionContext = mock[ExecutionContext]

      val results = action.executeUntyped(List.empty)(executionContext)
      results.length shouldBe 1
      results(0) shouldBe a[CustomTransformer]
      val result  = results(0)

      result.parameters.length shouldBe 2

      result.parameters(0).name shouldBe "public param 1"
      result.parameters(0).parameterType shouldBe ParameterType.Choice

      result.parameters(1).name shouldBe "public param 2"
      result.parameters(1).parameterType shouldBe ParameterType.Choice
    }

    "create CustomTransformer without public parameters" in {
      val action = new CreateCustomTransformer()
      val executionContext = mock[ExecutionContext]

      val results = action.executeUntyped(List.empty)(executionContext)
      results.size shouldBe 1
      results(0) shouldBe a[CustomTransformer]
      val result = results(0)

      result.parameters.length shouldBe 0
    }

    "infer parameters of CustomTransformer from input inner workflow" in {
      val action = MockCreateCustomTransformer.createWithParameter
      val inferContext = mock[InferContext]

      val results = action.inferKnowledgeUntyped(List.empty)(inferContext)._1.map(_.single)
      results.length shouldBe 1
      results(0) shouldBe a[CustomTransformer]
      val result  = results(0).asInstanceOf[CustomTransformer]

      result.parameters.length shouldBe 2

      result.parameters(0).name shouldBe "public param 1"
      result.parameters(0).parameterType shouldBe ParameterType.Choice

      result.parameters(1).name shouldBe "public param 2"
      result.parameters(1).parameterType shouldBe ParameterType.Choice
    }
  }

  private def createInnerWorkflow(publicParameters: PublicParameter*): InnerWorkflow = {
    val sourceNodeId = "2603a7b5-aaa9-40ad-9598-23f234ec5c32"
    val sinkNodeId = "d7798d5e-b1c6-4027-873e-a6d653957418"

    val sourceNode = Node(sourceNodeId, new Source())
    val sinkNode = Node(sinkNodeId, new Sink())

    val node1Action = {
      val parameters = TypeConverterInfo
        .setTargetType(TargetTypeChoices.StringTargetTypeChoice())
        .setSelectedColumns(MultipleColumnSelection(List(NameColumnSelection(Set("column1")))))
        .parameterValuesToJson
      new ConvertType().setParametersFromJson(parameters)
    }

    val node2Action = {
      val parameters = TypeConverterInfo
        .setTargetType(TargetTypeChoices.StringTargetTypeChoice())
        .setSelectedColumns(MultipleColumnSelection(List(NameColumnSelection(Set("column1")))))
        .parameterValuesToJson
      new ConvertType().setParametersFromJson(parameters)
    }

    val node1 = Node(node1Id, node1Action)
    val node2 = Node(node2Id, node2Action)

    val simpleGraph = FlowGraph(
      Set(sourceNode, sinkNode, node1, node2),
      Set(Edge((sourceNode, 0), (node1, 0)), Edge((node1, 0), (node2, 0)), Edge((node2, 0), (sinkNode, 0)))
    )

    InnerWorkflow(simpleGraph, Json.Null, publicParameters.toList)
  }
}
