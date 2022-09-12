package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.serialization.{ParametersSerialization, SerializableSparkModel}
import com.harana.sdk.backend.models.flow.inference.exceptions.TransformSchemaError
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterMap, ParameterPair}
import org.apache.spark.ml
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.types.StructType

abstract class SparkModelWrapper[M <: ml.Model[M], E <: ml.Estimator[M]]
  extends Transformer
  with ParametersWithSparkWrappers {

  var serializableModel: SerializableSparkModel[M] = _
  var parentEstimator: SparkEstimatorWrapper[M, E, _] = _

  def sparkModel = serializableModel.sparkModel

  def setModel(model: SerializableSparkModel[M]): this.type = {
    this.serializableModel = model
    this
  }

  def setParent(estimator: SparkEstimatorWrapper[M, E, _]): this.type = {
    parentEstimator = estimator
    set(estimator.extractParameterMap())
  }
  override def setDefault[T](param: Parameter[T], value: T): this.type = this
  override def setDefault(paramPairs: ParameterPair[_]*): this.type = this

  def applyTransform(ctx: ExecutionContext, df: DataFrame) =
    DataFrame.fromSparkDataFrame(serializableModel.transform(df.sparkDataFrame, sparkParamMap(df.sparkDataFrame.schema)))

  override def applyTransformSchema(schema: StructType) =
    if (parentEstimator == null)
      None
    else {
      val sparkEstimatorWithParameters = parentEstimator.serializableEstimator
        .copy(parentEstimator.sparkParamMap(parentEstimator.sparkEstimator, schema))
        .copy(sparkParamMap(parentEstimator.sparkEstimator, schema))
      try
        Some(sparkEstimatorWithParameters.transformSchema(schema))
      catch {
        case e: Exception => throw TransformSchemaError(e.getMessage, Some(e)).toException
      }
    }

  def sparkParamMap(schema: StructType): ml.param.ParamMap =
    super.sparkParamMap(sparkModel, schema)

  override def replicate(extra: ParameterMap): this.type = {
    val replicatedEstimatorWrapper = if (parentEstimator == null) null else parentEstimator.replicate(extractParameterMap(extra))

    val modelCopy = Option(serializableModel)
      .map(m => m.copy(m.extractParamMap()).setParent(parentEstimator.serializableEstimator)).orNull
      .asInstanceOf[SerializableSparkModel[M]]

    val replicated = super.replicate(extractParameterMap(extra)).setModel(modelCopy)
    if (parentEstimator == null) replicated else replicated.setParent(replicatedEstimatorWrapper)
  }

  override def loadTransformer(ctx: ExecutionContext, path: String): this.type =
    this.setParent(loadParentEstimator(ctx, path)).setModel(loadModel(ctx, Transformer.modelFilePath(path)))

  override def saveTransformer(ctx: ExecutionContext, path: String) = {
    saveModel(path)
    saveParentEstimator(ctx, path)
  }

  private def saveModel(path: String) = {
    val modelPath = Transformer.modelFilePath(path)
    serializableModel.save(modelPath)
  }

  private def saveParentEstimator(ctx: ExecutionContext, path: String) = {
    val parentEstimatorFilePath = Transformer.parentEstimatorFilePath(path)
    parentEstimator.saveObjectWithParameters(ctx, parentEstimatorFilePath)
  }

  private def loadParentEstimator(ctx: ExecutionContext, path: String) =
    ParametersSerialization.load(ctx, Transformer.parentEstimatorFilePath(path)).asInstanceOf[SparkEstimatorWrapper[M, E, _]]

  def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[M]

}