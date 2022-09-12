package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.shared.models.flow.actionobjects.SortColumnParameter.{columnNameParameterName, descendingFlagParameterName}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}
import com.harana.sdk.shared.models.flow.actionobjects.{SortColumnParameter, SortTransformerInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.parameters.selections.{IndexSingleColumnSelection, NameSingleColumnSelection, SingleColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameters, ParametersSequence, SingleColumnSelectorParameter}
import org.apache.spark.sql.Column
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.StructType

class SortTransformer extends Transformer with SortTransformerInfo {

  def applyTransform(ctx: ExecutionContext, df: DataFrame) =
    getColumns match {
      case Nil => df
      case _ => DataFrame.fromSparkDataFrame(df.sparkDataFrame.sort(getColumns.map(columnParameterToColumnExpression(_, df)): _*))
    }

   override def applyTransformSchema(schema: StructType) = {
    getSelectedSortColumnNames(schema, _.getColumnName)
    Some(schema)
  }

  private def getSelectedSortColumnNames(schema: StructType, selector: SortColumnParameter => SingleColumnSelection) =
    getColumns.map(columnPair => DataFrameColumnsGetter.getColumnName(schema, selector(columnPair)))

  def columnParameterToColumnExpression(scp: SortColumnParameter, df: DataFrame): Column = {
    val column = col(DataFrameColumnsGetter.getColumnName(df.schema.get, scp.getColumnName))
    if (scp.getDescending) {
      column.desc
    } else {
      column.asc
    }
  }
}