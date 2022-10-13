package com.harana.sdk.backend.models.flow.actiontypes

import org.apache.spark.sql.types.StructType
import org.scalatest.matchers.should.Matchers
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects._
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge, Method1To1}
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.designer
import com.harana.sdk.shared.models.flow.actionobjects.MetricValue
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.parameters.{NumericParameter, Parameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

object MockActionObjectsFactory$Type extends UnitSpec with TestSupport {

  val DefaultForA = 1

  def mockTransformer(df: DataFrame, dfk: Knowledge[DataFrame]) = {
    val t = mock[Transformer]
    val transform = mock[Method1To1[Unit, DataFrame, DataFrame]]
    when(transform.apply(any())(any())(any())).thenReturn(df)
    when(transform.infer(any())(any())(any())).thenReturn((dfk, InferenceWarnings.empty))
    when(t.transform).thenReturn(transform)
    t
  }

  def dataFrameKnowledge(s: StructType) = Knowledge(Seq(DataFrame.forInference(s)))

  val transformedDataFrame1 = createDataFrame()
  val transformedDataFrame2 = createDataFrame()

  val transformedDataFrameSchema1 = transformedDataFrame1.schema.get
  val transformedDataFrameSchema2 = transformedDataFrame2.schema.get

  val transformedDataFrameKnowledge1 = dataFrameKnowledge(transformedDataFrameSchema1)
  val transformedDataFrameKnowledge2 = dataFrameKnowledge(transformedDataFrameSchema2)

  val transformer1 = mockTransformer(transformedDataFrame1, transformedDataFrameKnowledge1)
  when(transformer1._transformSchema(any(), any())).thenReturn(Some(transformedDataFrameSchema1))

  val transformer2 = mockTransformer(transformedDataFrame2, transformedDataFrameKnowledge2)
  when(transformer2._transformSchema(any(), any())).thenReturn(Some(transformedDataFrameSchema2))

  val transformerKnowledge1 = Knowledge(transformer1)
  val transformerKnowledge2 = Knowledge(transformer2)

  val metricValue1 = MetricValue("name1", 0.1)
  val metricValue2 = MetricValue("name2", 0.2)

  val metricValueKnowledge1 = Knowledge(MetricValue.forInference("name1"))
  val metricValueKnowledge2 = Knowledge(MetricValue.forInference("name2"))

  class MockEstimator extends Estimator[Transformer] {
    val id = "test"

    val paramA = NumericParameter("b", default = Some(DefaultForA), validator = RangeValidator(0.0, Double.MaxValue))
    override val parameterGroups = List(ParameterGroup("", paramA))

    override def report(extended: Boolean = true) = ???

    override def fit: Method1To1[Unit, DataFrame, Transformer] = {
      new Method1To1[Unit, DataFrame, Transformer] {
        def apply(context: ExecutionContext)(parameters: Unit)(t0: DataFrame) = {
          $(paramA) match {
            case 1      => transformer1
            case -2 | 2 => transformer2
          }
        }

        override def infer(context: InferContext)(parameters: Unit)(k0: Knowledge[DataFrame]): (Knowledge[Transformer], InferenceWarnings) = {
          $(paramA) match {
            case 1      => (transformerKnowledge1, InferenceWarnings.empty)
            case -2 | 2 => (transformerKnowledge2, InferenceWarnings.empty)
          }
        }
      }
    }

    override def _fit_infer(schema: Option[StructType]) = transformer1

    // Not used in tests.
    override def _fit(ctx: ExecutionContext, df: DataFrame) = ???

  }

  class MockEvaluator extends Evaluator {
    val id = "test"

    val paramA = NumericParameter("b", default = Some(DefaultForA), validator = RangeValidator(0.0, Double.MaxValue))
    override val parameterGroups = List(ParameterGroup("", paramA))

    override def report(extended: Boolean = true) = ???

     override def evaluate: Method1To1[Unit, DataFrame, MetricValue] = {
      new Method1To1[Unit, DataFrame, MetricValue] {
        def apply(ctx: ExecutionContext)(p: Unit)(dataFrame: DataFrame): MetricValue = {
          $(paramA) match {
            case 1 => metricValue1
            case 2 => metricValue2
          }
        }

        override def infer(ctx: InferContext)(p: Unit)(k: Knowledge[DataFrame]): (Knowledge[MetricValue], InferenceWarnings) = {
          $(paramA) match {
            case 1 => (metricValueKnowledge1, InferenceWarnings.empty)
            case 2 => (metricValueKnowledge2, InferenceWarnings.empty)
          }
        }
      }
    }

    override def _infer(k: Knowledge[DataFrame]) = MetricValue.forInference("name1")

    // Not used in tests.
    def _evaluate(context: ExecutionContext, dataFrame: DataFrame): MetricValue = ???

    def isLargerBetter: Boolean = ???

  }
}
