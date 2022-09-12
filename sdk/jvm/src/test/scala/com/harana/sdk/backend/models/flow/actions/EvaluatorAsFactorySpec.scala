package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.designer.flow.Action._
import com.harana.sdk.backend.models.designer.flow.{ExecutionContext, Knowledge, ReportTypeDefault, UnitSpec}
import com.harana.sdk.backend.models.designer.flow.actionobjects.Evaluator
import com.harana.sdk.backend.models.designer.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.designer.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.designer.flow.parameters
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.actionobjects.MetricValue
import com.harana.sdk.shared.models.flow.parameters.{NumericParameter, ParameterMap}
import com.harana.sdk.shared.models.flow.utils.Id

class EvaluatorAsFactorySpec extends UnitSpec {

  import EvaluatorAsFactorySpec._

  "EvaluatorAsFactory" should {
    "have the same parameters as the Evaluator" in {
      val mockEvaluator = new MockEvaluator
      val mockFactory   = new MockEvaluatorFactory
      val reportType    = ReportTypeDefault(mockFactory.reportTypeParameter)
      mockFactory.extractParameterMap() shouldBe mockEvaluator.extractParameterMap() ++ ParameterMap(reportType)
      mockFactory.parameters shouldBe mockEvaluator.parameters
    }

    val paramValue1 = 100
    val paramValue2 = 1337

    "produce an Evaluator with parameters set" in {
      val mockFactory = new MockEvaluatorFactory
      mockFactory.set(mockFactory.evaluator.param -> paramValue1)
      val Vector(evaluator: MockEvaluator) = mockFactory.executeUntyped(Vector.empty)(mock[ExecutionContext])

      evaluator.get(mockFactory.evaluator.param) shouldBe Some(paramValue1)
    }

    "propagate parameters to wrapped evaluator" in {
      val mockFactory = new MockEvaluatorFactory
      mockFactory.set(mockFactory.evaluator.param -> paramValue1)
      val evaluator1 = execute(mockFactory)
      evaluator1.get(mockFactory.evaluator.param) shouldBe Some(paramValue1)

      mockFactory.set(mockFactory.evaluator.param -> paramValue2)
      val evaluator2 = execute(mockFactory)
      evaluator2.get(mockFactory.evaluator.param) shouldBe Some(paramValue2)
    }

    "infer knowledge" in {
      val mockFactory = new MockEvaluatorFactory
      mockFactory.set(mockFactory.evaluator.param -> paramValue1)

      val (Vector(knowledge), warnings) = mockFactory.inferKnowledgeUntyped(Vector.empty)(mock[InferContext])

      knowledge should have size 1
      knowledge.single shouldBe a[MockEvaluator]
      val evaluator = knowledge.single.asInstanceOf[MockEvaluator]
      evaluator.extractParameterMap() shouldBe execute(mockFactory).extractParameterMap()

      warnings shouldBe InferenceWarnings.empty
    }
  }

  private def execute(factory: MockEvaluatorFactory): MockEvaluator =
    factory.executeUntyped(Vector.empty)(mock[ExecutionContext]).head.asInstanceOf[MockEvaluator]

}

object EvaluatorAsFactorySpec {

  class MockEvaluator extends Evaluator {
    val id = "test"
    val param = NumericParameter("b", Some("desc"))
    setDefault(param -> 5)
    val parameters = Array(param)

    override def _evaluate(ctx: ExecutionContext, df: DataFrame): MetricValue = ???
    override def _infer(k: Knowledge[DataFrame]): MetricValue = ???
    override def report(extended: Boolean = true) = ???

    def isLargerBetter: Boolean = ???

  }

  class MockEvaluatorFactory extends EvaluatorAsFactory[MockEvaluator] {
    val id: Id = Id.randomId
    val name = "Mock Evaluator factory used for tests purposes"
    val description = "Description"
  }
}
