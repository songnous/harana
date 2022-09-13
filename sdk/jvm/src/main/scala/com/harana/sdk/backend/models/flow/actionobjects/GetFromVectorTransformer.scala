package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow.actionobjects.GetFromVectorTransformerInfo
import com.harana.sdk.shared.models.flow.utils.ColumnType
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

class GetFromVectorTransformer extends MultiColumnTransformer with GetFromVectorTransformerInfo {

  def transformSingleColumn(inputColumn: String, outputColumn: String, context: ExecutionContext, dataFrame: DataFrame) = {
    val inputColumnIndex = dataFrame.schema.get.fieldIndex(inputColumn)
    val indexInVector = getIndex
    val transformedRdd = dataFrame.sparkDataFrame.rdd.map { r =>
      val vector = r.get(inputColumnIndex).asInstanceOf[com.harana.spark.Linalg.Vector]
      if (vector != null) Row.fromSeq(r.toSeq :+ vector.apply(indexInVector)) else Row.fromSeq(r.toSeq :+ null)
    }
    val expectedSchema = transformSingleColumnSchema(inputColumn, outputColumn, dataFrame.schema.get).get
    val transformedDataFrame = context.sparkSQLSession.createDataFrame(transformedRdd, expectedSchema)
    DataFrame.fromSparkDataFrame(transformedDataFrame)
  }

  def transformSingleColumnSchema(inputColumn: String, outputColumn: String, schema: StructType): Option[StructType] = {
    MultiColumnTransformer.assertColumnExist(inputColumn, schema)
    DataFrame.assertExpectedColumnType(schema(inputColumn), ColumnType.Vector)
    MultiColumnTransformer.assertColumnDoesNotExist(outputColumn, schema)
    Some(schema.add(StructField(outputColumn, DoubleType)))
  }
}