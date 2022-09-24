package com.harana.sdk.backend.models.flow.actionobjects.wrappers

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.parameters.wrappers.ParamWrapper
import com.harana.spark.ML
import org.apache.spark.ml.param.{Param, ParamMap}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql
import org.apache.spark.sql.types.StructType

class TransformerWrapper(executionContext: ExecutionContext, transformer: Transformer) extends ML.Model[TransformerWrapper] {

  def copy(extra: ParamMap): TransformerWrapper = {
    val parameters = ParamTransformer.transform(extra)
    val transformerCopy = transformer.replicate().set(parameters: _*)
    new TransformerWrapper(executionContext, transformerCopy)
  }

  def transformDF(dataset: sql.DataFrame): sql.DataFrame =
    transformer._transform(executionContext, DataFrame.fromSparkDataFrame(dataset.toDF())).sparkDataFrame

  def transformSchema(schema: StructType): StructType =
    transformer._transformSchema(schema).get

  override lazy val params: Array[Param[_]] =
    transformer.allParameters.map(new ParamWrapper(uid, _)).toArray

  val uid = Identifiable.randomUID("TransformerWrapper")

}
