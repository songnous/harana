package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.parameters.ParameterMap
import com.harana.sdk.shared.models.flow.utils.Id

class TransformerAsFactorySpec extends UnitSpec {

  import MockTransformers._

  class MockTransformerAsFactory extends TransformerAsFactory[MockTransformer] {
    val name = ""
    val id: Id = "6d924962-9456-11e5-8994-feff819cdc9f"
    }

  "TransformerAsFactory" should {
    def action: MockTransformerAsFactory = new MockTransformerAsFactory

    "have parameters same as Transformer" in {
      val op = action
      op.parameters shouldBe Array(op.transformer.paramA)
    }

    "have report type param set to extended" in {
      val op = action
      op.extractParameterMap().get(op.reportTypeParameter).get shouldBe Action.ReportParameter.Extended()
    }

    "have defaults same as in Transformer" in {
      val op = action
      val transformerParam = op.transformer.paramA -> DefaultForA
      op.extractParameterMap() shouldBe ParameterMap(transformerParam, ReportTypeDefault(op.reportTypeParameter))
    }

    "produce transformer with properly set parameters" in {
      val op = action
      op.set(op.transformer.paramA -> 2)

      val result = op.executeUntyped(Vector())(mock[ExecutionContext])
      (result should have).length(1)
      result(0).asInstanceOf[MockTransformer].extractParameterMap() shouldBe ParameterMap(op.transformer.paramA -> 2, ReportTypeDefault(op.reportTypeParameter))
    }

    "infer knowledge" in {
      val op = action
      op.set(op.transformer.paramA -> 2)

      val (result, warnings) = op.inferKnowledgeUntyped(Vector(Knowledge()))(mock[InferContext])
      warnings shouldBe InferenceWarnings.empty
      (result should have).length(1)
      result(0).single.asInstanceOf[MockTransformer].extractParameterMap() shouldBe ParameterMap(op.transformer.paramA -> 2, ReportTypeDefault(op.reportTypeParameter))
    }
  }
}
