package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.{SparkEstimatorWrapper, SparkModelWrapper}
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, Parameters, SingleColumnCreatorParameter}
import com.harana.spark.ML
import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.ml
import org.apache.spark.ml.param.{ParamMap, Param => SparkParam}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{IntegerType, StructField, StructType}

import scala.language.reflectiveCalls

object EstimatorModelWrapperFixtures {

  class SimpleSparkModel private[EstimatorModelWrapperFixtures]() extends ML.Model[SimpleSparkModel] {

    def this(x: String) = this()
    val uid = "modelId"

    val predictionColumnParameter = new SparkParam[String](uid, "name", "description")
    def getPredictionColumn = $(predictionColumnParameter)
    def setPredictionCol(value: String): this.type = set(predictionColumnParameter, value)


    def copy(extra: ParamMap) = defaultCopy(extra)

    def transformDF(dataset: DataFrame) = dataset.selectExpr("*", "1 as " + getPredictionColumn)

    @DeveloperApi
    def transformSchema(schema: StructType): StructType = ???

  }

  class SimpleSparkEstimator extends ML.Estimator[SimpleSparkModel] {

    def this(x: String) = this()
    val uid = "estimatorId"

    val predictionColumnParameter = new SparkParam[String](uid, "name", "description")
    def getPredictionColumn = $(predictionColumnParameter)
    def setPredictionCol(value: String): this.type = set(predictionColumnParameter, value)

    def fitDF(dataset: DataFrame) = new SimpleSparkModel().setPredictionCol(getPredictionColumn)

    def copy(extra: ParamMap): ML.Estimator[SimpleSparkModel] = defaultCopy(extra)

    @DeveloperApi
    def transformSchema(schema: StructType) = schema.add(StructField(getPredictionColumn, IntegerType, nullable = false))
  }

  trait HasPredictionColumn extends Parameters {
    val predictionColumnParameter = SingleColumnCreatorParameter[ml.param.Params { val predictionCol: SparkParam[String] }]("prediction column", None, _.predictionCol)
    setDefault(predictionColumnParameter, "abcdefg")
    def getPredictionColumn = $(predictionColumnParameter)
    def setPredictionColumn(value: String): this.type = set(predictionColumnParameter, value)
  }

  class SimpleSparkModelWrapper extends SparkModelWrapper[SimpleSparkModel, SimpleSparkEstimator] with HasPredictionColumn {
    override val parameterGroups = List(ParameterGroup("", predictionColumnParameter))

    override def report(extended: Boolean = true) = ???
    def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SimpleSparkModel] = ???
  }

  class SimpleSparkEstimatorWrapper
      extends SparkEstimatorWrapper[SimpleSparkModel, SimpleSparkEstimator, SimpleSparkModelWrapper]
      with HasPredictionColumn {

    override val parameterGroups = List(ParameterGroup("", predictionColumnParameter))

    override def report(extended: Boolean = true) = ???

  }
}