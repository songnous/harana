package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.{ExecutionContext, TestSupport}
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, ParameterGroup, Parameters}
import com.harana.spark.ML
import org.apache.spark.ml
import org.apache.spark.ml.param.{BooleanParam, DoubleParam, ParamMap}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame => SparkDataFrame}
import org.scalatestplus.mockito.MockitoSugar

import scala.language.reflectiveCalls

object EstimatorModelWrappersFixtures extends MockitoSugar with TestSupport {

  trait HasNumericParameter extends Parameters {
    val numericParameter = DoubleParameter("name", default = Some(1.0))
  }

  class ExampleSparkEstimatorWrapper extends SparkEstimatorWrapper[ExampleSparkModel, ExampleSparkEstimator, ExampleSparkModelWrapper] with HasNumericParameter {
    val id = "test"
    def setNumericParamWrapper(value: Double): this.type = set(numericParameter, value)
    override def report(extended: Boolean = true) = ???
    override val parameterGroups = List(ParameterGroup(None, numericParameter))
  }

  class ExampleSparkEstimator extends ML.Estimator[ExampleSparkModel] {
    val uid = "estimatorId"

    val numericParameter = new DoubleParam(uid, "numeric", "description")
    def setNumericParameter(value: Double): this.type = set(numericParameter, value)

    def fitDF(dataset: SparkDataFrame): ExampleSparkModel = {
      require($(numericParameter) == paramValueToSet)
      fitModel
    }

    val transformSchemaShouldThrowParameter = new BooleanParam(uid, "throwing", "description")
    def setTransformSchemaShouldThrow(b: Boolean): this.type = set(transformSchemaShouldThrowParameter, b)

    def transformSchema(schema: StructType): StructType = {
      if ($(transformSchemaShouldThrowParameter)) throw exceptionThrownByTransformSchema
      require($(numericParameter) == paramValueToSet)
      transformedSchema
    }

    def copy(extra: ParamMap): ml.Estimator[ExampleSparkModel] = defaultCopy(extra)
  }

  class ExampleSparkModel extends ML.Model[ExampleSparkModel] {
    val uid = "modelId"

    val numericParameter = new DoubleParam(uid, "name", "description")
    def getNumericParameter = $(numericParameter)
    def setNumericParameter(value: Double): this.type = set(numericParameter, value)

    def copy(extra: ParamMap): ExampleSparkModel =
      extra.toSeq.foldLeft(new ExampleSparkModel())((model, paramPair) => model.set(paramPair))

    def transformDF(dataset: SparkDataFrame): SparkDataFrame = {
      require(getNumericParameter == paramValueToSet)
      fitDataFrame
    }

    def transformSchema(schema: StructType): StructType = ???

  }

  class ExampleSparkModelWrapper extends SparkModelWrapper[ExampleSparkModel, ExampleSparkEstimator] with HasNumericParameter {
    val id = "test"
    def setNumericParameter(value: Double): this.type = set(numericParameter, value)
    override def report(extended: Boolean = true) = ???
    override val parameterGroups = List(ParameterGroup(None, numericParameter))
    def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[ExampleSparkModel] = ???
  }

  val fitModel = new ExampleSparkModel()
  val fitDataFrame = createSparkDataFrame()
  val transformedSchema = createSchema()
  val paramValueToSet = 12.0
  val exceptionThrownByTransformSchema = new Exception("mock exception")
}
