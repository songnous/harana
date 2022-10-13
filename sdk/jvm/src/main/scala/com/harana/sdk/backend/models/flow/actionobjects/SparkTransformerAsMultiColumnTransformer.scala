package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.exceptions.TransformSchemaError
import com.harana.sdk.backend.models.flow.utils.TypeUtils
import izumi.reflect.Tag
import org.apache.spark.ml.{Transformer => SparkTransformer}
import org.apache.spark.sql.types._

import scala.language.reflectiveCalls
import scala.reflect.runtime.universe._

abstract class SparkTransformerAsMultiColumnTransformer[T <: SparkTransformer {
      def setInputCol(value: String): T
      def setOutputCol(value: String): T
    }](implicit tag: Tag[T])
    extends MultiColumnTransformer
    with ParametersWithSparkWrappers {

  lazy val sparkTransformer: T = TypeUtils.instanceOfType(tag)

  def convertInputNumericToVector: Boolean = false
  def convertOutputVectorToDouble: Boolean = false

  def transformSingleColumn(inputColumn: String, outputColumn: String, context: ExecutionContext, dataFrame: DataFrame) = {
    val transformer = sparkTransformerWithParameters(dataFrame.sparkDataFrame.schema)
    transformer.setInputCol(inputColumn)
    transformer.setOutputCol(outputColumn)
    if (convertInputNumericToVector && NumericToVectorUtils.isColumnNumeric(dataFrame.schema.get, inputColumn)) {
      val convertedDf = NumericToVectorUtils.convertDataFrame(dataFrame, inputColumn, context)
      val transformedDf = transformer.transform(convertedDf)
      val expectedSchema = transformSingleColumnSchema(inputColumn, outputColumn, dataFrame.schema.get).get
      val revertedTransformedDf = NumericToVectorUtils.revertDataFrame(transformedDf, expectedSchema, inputColumn, outputColumn, context, convertOutputVectorToDouble)
      DataFrame.fromSparkDataFrame(revertedTransformedDf)
    } else
      DataFrame.fromSparkDataFrame(transformer.transform(dataFrame.sparkDataFrame))
  }

  def transformSingleColumnSchema(inputColumn: String, outputColumn: String, schema: StructType): Option[StructType]  = {
    val transformer = sparkTransformerWithParameters(schema)
    transformer.setInputCol(inputColumn)
    transformer.setOutputCol(outputColumn)
    try {
      if (convertInputNumericToVector && NumericToVectorUtils.isColumnNumeric(schema, inputColumn)) {
        val convertedSchema = NumericToVectorUtils.convertSchema(schema, inputColumn)
        val transformedSchema = transformer.transformSchema(convertedSchema)
        val revertedTransformedSchema = NumericToVectorUtils.revertSchema(transformedSchema, inputColumn, outputColumn, convertOutputVectorToDouble)
        Some(revertedTransformedSchema)
      } else {
        val transformedSchema = transformer.transformSchema(schema)
        Some(transformedSchema)
      }
    } catch {
      case e: Exception => throw TransformSchemaError(e.getMessage, Some(e)).toException
    }
  }

  private def sparkTransformerWithParameters(schema: StructType) =
    sparkTransformer.copy(sparkParamMap(sparkTransformer, schema)).asInstanceOf[T]
}