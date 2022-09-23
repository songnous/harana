package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.{Estimator, Transformer}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.{ExecutionContext, UnitSpec}
import com.harana.sdk.shared.models.flow.parameters.NumericParameter
import com.harana.sdk.shared.models.flow.utils.Id
import org.apache.spark.sql.types.StructType

class EstimatorAsFactorySpec extends UnitSpec {

  import EstimatorAsFactorySpec._

  "EstimatorAsFactory" should {

    "have the same parameters as the Estimator" in {
      val mockEstimator = new MockEstimator
      val mockFactory = new MockEstimatorFactory
      val reportTypeParameterMap = ParameterMap(ReportTypeDefault(mockFactory.reportTypeParameter))
      mockFactory.extractParameterMap() shouldBe mockEstimator.extractParameterMap() ++ reportTypeParameterMap
      mockFactory.parameters shouldBe mockEstimator.parameters
    }
    val paramValue1 = 100
    val paramValue2 = 1337

    "produce an Estimator with parameters set" in {
      val mockFactory = new MockEstimatorFactory
      mockFactory.set(mockFactory.estimator.param -> paramValue1)
      val List(estimator: MockEstimator) = mockFactory.executeUntyped(List.empty)(mock[ExecutionContext])
      estimator.get(mockFactory.estimator.param) shouldBe Some(paramValue1)
    }

    "return the same instance of estimator each time" in {
      val mockFactory = new MockEstimatorFactory
      mockFactory.set(mockFactory.estimator.param -> paramValue1)
      val estimator1 = execute(mockFactory)
      estimator1.get(mockFactory.estimator.param) shouldBe Some(paramValue1)

      mockFactory.set(mockFactory.estimator.param -> paramValue2)
      val estimator2 = execute(mockFactory)
      estimator2.get(mockFactory.estimator.param) shouldBe Some(paramValue2)
    }

    "infer knowledge" in {
      val mockFactory = new MockEstimatorFactory
      mockFactory.set(mockFactory.estimator.param -> paramValue1)

      val (List(knowledge), warnings) = mockFactory.inferKnowledgeUntyped(List.empty)(mock[InferContext])

      knowledge should have size 1
      knowledge.single shouldBe a[MockEstimator]
      val estimator = knowledge.single
      estimator.extractParameterMap() shouldBe execute(mockFactory).extractParameterMap()

      warnings shouldBe InferenceWarnings.empty
    }
  }

  private def execute(factory: MockEstimatorFactory): MockEstimator =
    factory.executeUntyped(List.empty)(mock[ExecutionContext]).head

}

object EstimatorAsFactorySpec {

  class MockEstimator extends Estimator[Transformer] {
    val id = "test"
    val param = NumericParameter("b")
    setDefault(param -> 5)
    val parameters = Left(Array(param))
    override def _fit(ctx: ExecutionContext, df: DataFrame) = ???
    override def _fit_infer(schema: Option[StructType]) = ???
    override def report(extended: Boolean = true) = ???
  }

  class MockEstimatorFactory extends EstimatorAsFactory[MockEstimator] {
    val id: Id = Id.randomId
    val name = "Mock Estimator factory used for tests purposes"
  }
}
