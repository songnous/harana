package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.spark.Linalg.Vectors
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame => SparkDataFrame}

object NumericToVectorUtils {

  private def updateSchema(schema: StructType, colName: String, dataType: DataType): StructType =
    updateSchema(schema, schema.fieldIndex(colName), dataType)

  private def updateSchema(schema: StructType, idx: Int, dataType: DataType): StructType =
    schema.copy(schema.fields.clone().updated(idx, schema(idx).copy(dataType = dataType)))

  def isColumnNumeric(schema: StructType, colName: String) =
    schema(colName).dataType.isInstanceOf[NumericType]

  def convertSchema(schema: StructType, inputColumn: String) =
    updateSchema(schema, inputColumn, new com.harana.spark.Linalg.VectorUDT())

  def revertSchema(schema: StructType, inputColumn: String, outputColumn: String, convertOutputVectorToDouble: Boolean) = {
    val unconvertedSchema = updateSchema(schema, inputColumn, DoubleType)
    if (convertOutputVectorToDouble) updateSchema(unconvertedSchema, outputColumn, DoubleType) else unconvertedSchema
  }

  def convertDataFrame(dataFrame: DataFrame, inputColumn: String, context: ExecutionContext) = {
    val inputColumnIdx  = dataFrame.schema.get.fieldIndex(inputColumn)
    val convertedRdd    = dataFrame.sparkDataFrame.rdd.map { r =>
      val value = r.get(inputColumnIdx)
      Row.fromSeq(r.toSeq.updated(inputColumnIdx, if (value != null) Vectors.dense(value.asInstanceOf[Double]) else null))
    }
    val convertedSchema = NumericToVectorUtils.convertSchema(dataFrame.schema.get, inputColumn)
    context.sparkSQLSession.createDataFrame(convertedRdd, convertedSchema)
  }

  def revertDataFrame(sdf: SparkDataFrame, expectedSchema: StructType, inputColumn: String, outputColumn: String, context: ExecutionContext, convertOutputVectorToDouble: Boolean) = {
    val inputColumnIdx              = sdf.schema.fieldIndex(inputColumn)
    val outputColumnIdx             = sdf.schema.fieldIndex(outputColumn)
    val extractFirstValueFromVector = (columnIdx: Int) =>
      (r: Row) => {
        val vector = r.get(columnIdx)
        Row.fromSeq(r.toSeq.updated(columnIdx, if (vector != null) vector.asInstanceOf[com.harana.spark.Linalg.Vector].apply(0) else null))
      }
    val transformedInputColumnRdd   = sdf.rdd.map(extractFirstValueFromVector(inputColumnIdx))
    val transformedRdd              =
      if (convertOutputVectorToDouble && inputColumnIdx != outputColumnIdx)
        transformedInputColumnRdd.map(extractFirstValueFromVector(outputColumnIdx))
      else
        transformedInputColumnRdd
    context.sparkSQLSession.createDataFrame(transformedRdd, expectedSchema)
  }
}
