package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}
import com.harana.sdk.shared.models.flow.actionobjects.ColumnsFiltererInfo
import com.harana.sdk.shared.models.flow.parameters.ColumnSelectorParameter
import com.harana.sdk.shared.models.flow.parameters.selections.{MultipleColumnSelection, NameColumnSelection}
import org.apache.spark.sql.types.StructType

class ColumnsFilterer extends Transformer with ColumnsFiltererInfo {

  def applyTransform(ctx: ExecutionContext, df: DataFrame) = {
    val columns = df.getColumnNames(getSelectedColumns)
    if (columns.isEmpty)
      DataFrame.empty(ctx)
    else {
      val filtered = df.sparkDataFrame.select(columns.head, columns.tail: _*)
      DataFrame.fromSparkDataFrame(filtered)
    }
  }

  override def applyTransformSchema(schema: StructType) = {
    val outputColumns  = DataFrameColumnsGetter.getColumnNames(schema, getSelectedColumns)
    val inferredSchema =
      if (outputColumns.isEmpty)
        StructType(Seq.empty)
      else {
        val fields = schema.filter(field => outputColumns.contains(field.name))
        StructType(fields)
      }
    Some(inferredSchema)
  }

}