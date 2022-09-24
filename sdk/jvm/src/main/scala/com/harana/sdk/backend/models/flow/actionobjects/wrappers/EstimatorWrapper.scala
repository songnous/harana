package com.harana.sdk.backend.models.flow.actionobjects.wrappers

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.{Estimator, Transformer}
import com.harana.sdk.backend.models.flow.parameters.wrappers.ParamWrapper
import com.harana.spark.ML
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.ml
import org.apache.spark.sql
import org.apache.spark.sql.types.StructType

class EstimatorWrapper(executionContext: ExecutionContext, estimator: Estimator[Transformer]) extends ML.Estimator[TransformerWrapper] {

  def fitDF(dataset: sql.DataFrame): TransformerWrapper = {
    new TransformerWrapper(
      executionContext,
      estimator._fit(executionContext, DataFrame.fromSparkDataFrame(dataset.toDF()))
    )
  }

  def copy(extra: ParamMap): EstimatorWrapper = {
    val parameters = ParamTransformer.transform(extra)
    val estimatorCopy = estimator.replicate().set(parameters: _*)
    new EstimatorWrapper(executionContext, estimatorCopy)
  }

  def transformSchema(schema: StructType): StructType =
    schema

  override lazy val params: Array[ml.param.Param[_]] =
    estimator.allParameters.map(new ParamWrapper(uid, _)).toArray

  val uid = Identifiable.randomUID("EstimatorWrapper")

}
