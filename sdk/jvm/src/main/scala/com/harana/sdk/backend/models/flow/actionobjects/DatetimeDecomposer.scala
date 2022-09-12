package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}
import com.harana.sdk.shared.models.flow.actionobjects.DatetimeDecomposerInfo
import com.harana.sdk.shared.models.flow.utils.ColumnType
import org.apache.spark.sql
import org.apache.spark.sql.Column
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}

class DatetimeDecomposer extends Transformer with DatetimeDecomposerInfo {

  def applyTransform(context: ExecutionContext, dataFrame: DataFrame) = {
    DataFrameColumnsGetter.assertExpectedColumnType(
      dataFrame.sparkDataFrame.schema,
      timestampColumn,
      ColumnType.Timestamp
    )

    val decomposedColumnName = dataFrame.getColumnName(timestampColumn)

    val newColumns = for {
      range <- DatetimeDecomposerInfo.timestampPartRanges
      if getTimestampParts.contains(range.part)
    } yield timestampUnitColumn(dataFrame.sparkDataFrame, decomposedColumnName, range)

    dataFrame.withColumns(context, newColumns)
  }

  private[this] def timestampUnitColumn(
      sparkDataFrame: sql.DataFrame,
      columnName: String,
      timestampPart: DatetimeDecomposerInfo.TimestampPartRange): Column = {

    val newColumnName = getTimestampPrefix + timestampPart.part.name

    sparkDataFrame(columnName)
      .substr(timestampPart.start, timestampPart.length)
      .as(newColumnName)
      .cast(DoubleType)
  }

  override def applyTransformSchema(schema: StructType): Option[StructType] = {
    DataFrameColumnsGetter.assertExpectedColumnType(schema, timestampColumn, ColumnType.Timestamp)

    val newColumns = for {
      range <- DatetimeDecomposerInfo.timestampPartRanges
      if getTimestampParts.contains(range.part)
    } yield StructField(getTimestampPrefix + range.part.name, DoubleType)

    val inferredSchema = StructType(schema.fields ++ newColumns)
    Some(inferredSchema)
  }
}