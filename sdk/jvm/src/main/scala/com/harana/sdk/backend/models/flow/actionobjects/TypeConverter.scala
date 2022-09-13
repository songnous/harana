package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.utils.SparkTypeConverter
import com.harana.sdk.shared.models.flow.actionobjects.TypeConverterInfo
import org.apache.spark.sql.types.{StructField => SparkStructField, StructType => SparkStructType}

case class TypeConverter() extends MultiColumnTransformer with TypeConverterInfo {

  def transformSingleColumn(inputColumn: String, outputColumn: String, context: ExecutionContext, dataFrame: DataFrame) = {
    val targetTypeName = getTargetType.columnType.getClass.getSimpleName
      .stripSuffix("$").stripSuffix("Type").stripSuffix("UDT").toLowerCase
    val expr = s"cast(`$inputColumn` as $targetTypeName) as `$outputColumn`"
    val sparkDataFrame = dataFrame.sparkDataFrame.selectExpr("*", expr)
    DataFrame.fromSparkDataFrame(sparkDataFrame)
  }

  def transformSingleColumnSchema(inputColumn: String, outputColumn: String, schema: SparkStructType): Option[SparkStructType] = {
    MultiColumnTransformer.assertColumnExist(inputColumn, schema)
    MultiColumnTransformer.assertColumnDoesNotExist(outputColumn, schema)
    Some(schema.add(SparkStructField(outputColumn, SparkTypeConverter.toSparkDataType(getTargetType.columnType), nullable = true)))
  }
}