package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}
import com.harana.sdk.shared.models.flow.actionobjects.DatetimeComposerInfo
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, MultipleChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{SingleColumnCreatorParameter, SingleColumnSelectorParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.SingleColumnSelection
import com.harana.sdk.shared.models.flow.utils.ColumnType
import org.apache.spark.sql.functions.concat_ws
import org.apache.spark.sql.functions.format_string
import org.apache.spark.sql.functions.lit
import org.apache.spark.sql.functions.unix_timestamp
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.TimestampType

class DatetimeComposer extends Transformer with DatetimeComposerInfo {

  import DatetimeComposerInfo._

  def applyTransform(context: ExecutionContext, dataFrame: DataFrame) = {
    val sparkDataFrame = dataFrame.sparkDataFrame
    val dataColumns = timestampColumns.map(p => p.name -> p.timestampColumn).toMap
    val newSchema = applyTransformSchema(sparkDataFrame.schema).get

    val partColumns = for { part <- orderedTimestampParts } yield dataColumns.get(part.name) match {
      case Some(singleColumnSelect) => format_string(part.formatString, sparkDataFrame(dataFrame.getColumnName(singleColumnSelect)).cast(DoubleType))
      case None => lit(part.formatString.format(part.defaultValue.toDouble))
    }

    val newColumn = unix_timestamp(
      concat_ws(
        " ",
        concat_ws("-", partColumns(0), partColumns(1), partColumns(2)),
        concat_ws(":", partColumns(3), partColumns(4), partColumns(5))
      )
    ).cast(TimestampType)

    // have to create dataFrame using schema for timestamp column to be nullable
    val appendedFrame = sparkDataFrame.withColumn(outputColumn, newColumn)
    DataFrame.fromSparkDataFrame(context.sparkSQLSession.createDataFrame(appendedFrame.rdd, newSchema))
  }

  override def applyTransformSchema(schema: StructType): Option[StructType] = {
    assertCorrectColumnTypes(schema)
    val newColumn = StructField(outputColumn, TimestampType, nullable = true)
    val inferredSchema = StructType(schema.fields :+ newColumn)
    Some(inferredSchema)
  }

  private def assertCorrectColumnTypes(schema: StructType) = {
    for { timestampPart <- timestampColumns } yield
      DataFrameColumnsGetter.assertExpectedColumnType(schema, timestampPart.timestampColumn, ColumnType.Numeric)
  }
}