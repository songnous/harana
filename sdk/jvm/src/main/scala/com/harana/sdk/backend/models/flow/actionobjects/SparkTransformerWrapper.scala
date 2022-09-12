package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.{ExecutionContext, actionobjects}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.exceptions.TransformSchemaError
import com.harana.sdk.shared.models.flow.utils.TypeUtils
import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import scala.reflect.runtime.universe._

abstract class SparkTransformerWrapper[T <: ml.Transformer](implicit tag: TypeTag[T])
  extends actionobjects.Transformer with ParametersWithSparkWrappers {

  lazy val sparkTransformer: T = TypeUtils.instanceOfType(tag)

  def applyTransform(ctx: ExecutionContext, df: DataFrame) = {
    val paramMap = sparkParamMap(sparkTransformer, df.sparkDataFrame.schema)
    DataFrame.fromSparkDataFrame(sparkTransformer.transform(df.sparkDataFrame, paramMap))
  }

  override def applyTransformSchema(schema: StructType) = {
    val paramMap = sparkParamMap(sparkTransformer, schema)
    val transformerForInference = sparkTransformer.copy(paramMap)

    try {
      Some(transformerForInference.transformSchema(schema))
    } catch {
      case e: Exception => throw TransformSchemaError(e.getMessage, Some(e)).toException
    }
  }
}