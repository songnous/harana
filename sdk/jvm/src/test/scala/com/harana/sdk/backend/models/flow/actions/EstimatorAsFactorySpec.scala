package com.harana.sdk.backend.models.flow.actions

import org.apache.spark.sql.types.StructType
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.backend.models.designer.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.designer.flow.actionobjects.Estimator
import com.harana.sdk.backend.models.designer.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.designer.flow.ExecutionContext
import com.harana.sdk.backend.models.designer.flow.reportTypeDefault
import com.harana.sdk.backend.models.designer.flow.UnitSpec
import com.harana.sdk.backend.models.designer.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.designer.flow.parameters
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.ActionInfo
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.parameters.{NumericParameter, Parameter, ParameterMap}
import com.harana.sdk.shared.models.flow.utils.Id

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
      val Vector(estimator: MockEstimator) = mockFactory.executeUntyped(Vector.empty)(mock[ExecutionContext])
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

      val (Vector(knowledge), warnings) = mockFactory.inferKnowledgeUntyped(Vector.empty)(mock[InferContext])

      knowledge should have size 1
      knowledge.single shouldBe a[MockEstimator]
      val estimator = knowledge.single.asInstanceOf[MockEstimator]
      estimator.extractParameterMap() shouldBe execute(mockFactory).extractParameterMap()

      warnings shouldBe InferenceWarnings.empty
    }
  }

  private def execute(factory: MockEstimatorFactory): MockEstimator =
    factory.executeUntyped(Vector.empty)(mock[ExecutionContext]).head.asInstanceOf[MockEstimator]

}

object EstimatorAsFactorySpec {

  class MockEstimator extends Estimator[Transformer] {
    val id = "test"
    val param = NumericParameter("b", Some("desc"))
    setDefault(param -> 5)
    val parameters = Array(param)
    override def _fit(ctx: ExecutionContext, df: DataFrame) = ???
    override def _fit_infer(schema: Option[StructType]) = ???
    override def report(extended: Boolean = true) = ???
  }

  class MockEstimatorFactory extends EstimatorAsFactory[MockEstimator] {
    val id: Id = Id.randomId
    val name = "Mock Estimator factory used for tests purposes"
    val description = "Description"
  }
}
