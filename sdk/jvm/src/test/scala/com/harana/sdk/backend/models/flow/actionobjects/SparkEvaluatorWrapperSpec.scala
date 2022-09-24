package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge, TestSupport, UnitSpec}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.exceptions.ColumnDoesNotExistError
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actions.exceptions.ColumnDoesNotExistError
import com.harana.sdk.shared.models.flow.actionobjects.MetricValue
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, SingleColumnSelectorParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.{NameSingleColumnSelection, SingleColumnSelection}
import com.harana.spark.ML
import org.apache.spark.ml.param.{DoubleParam, Param, ParamMap}
import org.apache.spark.{ml, sql}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

import scala.language.reflectiveCalls

class SparkEvaluatorWrapperSpec extends UnitSpec with TestSupport {

  import SparkEvaluatorWrapperSpec._

  "SparkEvaluatorWrapper" should {

    "evaluate a DataFrame" in {
      val wrapper = ExampleEvaluatorWrapper().setParamWrapper(metricValue)
      val inputDataFrame = mockInputDataFrame

      val value = wrapper._evaluate(mock[ExecutionContext], inputDataFrame)
      value shouldBe MetricValue("test", metricValue)
    }

    "infer knowledge" in {
      val wrapper = ExampleEvaluatorWrapper().setParamWrapper(metricValue)
      val inferredValue = wrapper._infer(Knowledge(DataFrame.forInference()))
      inferredValue.name shouldBe metricName
    }

    "validate parameters" in {
      val wrapper = ExampleEvaluatorWrapper().setColumnWrapper(NameSingleColumnSelection("invalid"))
      val inputDataFrame = mockInputDataFrame

      a[ColumnDoesNotExistError] should be thrownBy {
        wrapper._evaluate(mock[ExecutionContext], inputDataFrame)
      }
    }

    "validate parameters during inference" in {
      val wrapper = ExampleEvaluatorWrapper().setColumnWrapper(NameSingleColumnSelection("invalid"))
      a[ColumnDoesNotExistError] should be thrownBy {
        wrapper._infer(Knowledge(mockInputDataFrame))
      }
    }
  }

  def mockInputDataFrame = {
    val schema = StructType(Seq(StructField("column", StringType)))
    createDataFrame(schema)
  }
}

object SparkEvaluatorWrapperSpec {
  val metricName = "test"
  val metricValue = 12.0

  case class ExampleEvaluatorWrapper() extends SparkEvaluatorWrapper[ExampleSparkEvaluator] {
    val id = "test"
    val paramWrapper = DoubleParameter("name", default = Some(0.0))
    def setParamWrapper(value: Double): this.type = set(paramWrapper, value)

    val column = SingleColumnSelectorParameter(name = "column", default = Some(NameSingleColumnSelection("column")), portIndex = 0)
    def setColumnWrapper(value: SingleColumnSelection): this.type = set(column, value)

    val parameters = Left(Array(paramWrapper, column))

    def getMetricName = metricName

    override def report(extended: Boolean = true) = ???

  }

  class ExampleSparkEvaluator extends ML.Evaluator {

    def this(id: String) = this()

    val uid = "evaluatorId"

    val numericParameter = new DoubleParam(uid, "numeric", "description")
    def setNumericParameter(value: Double): this.type = set(numericParameter, value)

    val columnParameter = new Param[String](uid, "string", "description")
    def setColumnParameter(value: String): this.type = set(columnParameter, value)

    def evaluateDF(dataset: sql.DataFrame) = $(numericParameter)

    def copy(extra: ParamMap): ml.evaluation.Evaluator = defaultCopy(extra)

  }
}