package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.serialization.{Loadable, ParametersSerialization, SerializableSparkEstimator}
import com.harana.sdk.backend.models.flow.utils.TypeUtils
import izumi.reflect.Tag
import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import scala.reflect.runtime.universe._

abstract class SparkEstimatorWrapper[M <: ml.Model[M], E <: ml.Estimator[M], MW <: SparkModelWrapper[M, E]](
    implicit val modelWrapperTag: Tag[MW],
    implicit val estimatorTag: Tag[E]
) extends Estimator[MW]
    with ParametersWithSparkWrappers
    with ParametersSerialization
    with Loadable {

  val serializableEstimator = createEstimatorInstance()
  def sparkEstimator = serializableEstimator.sparkEstimator

  override def _fit(ctx: ExecutionContext, dataFrame: DataFrame) = {
    val sparkParameters = sparkParamMap(sparkEstimator, dataFrame.sparkDataFrame.schema)
    val sparkModel = serializableEstimator.fit(dataFrame.sparkDataFrame, sparkParameters)
    createModelWrapperInstance().setModel(sparkModel).setParent(this)
  }

  override def _fit_infer(maybeSchema: Option[StructType]) = {
    validateSparkEstimatorParameters(sparkEstimator, maybeSchema)
    createModelWrapperInstance().setParent(this)
  }

  def createEstimatorInstance() = new SerializableSparkEstimator[M, E](TypeUtils.instanceOfType(estimatorTag))
  def createModelWrapperInstance(): MW = TypeUtils.instanceOfType(modelWrapperTag)
  def load(ctx: ExecutionContext, path: String) = loadAndSetParameters(ctx, path)

}