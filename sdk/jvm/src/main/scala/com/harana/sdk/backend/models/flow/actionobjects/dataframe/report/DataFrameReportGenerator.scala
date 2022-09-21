package com.harana.sdk.backend.models.flow.actionobjects.dataframe.report

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.distribution.{DistributionCalculator, NoDistributionReasons}
import com.harana.sdk.backend.models.flow.actionobjects.report.ReportUtils
import com.harana.sdk.backend.models.flow.utils.SparkTypeConverter
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.report._
import com.harana.sdk.shared.models.flow.utils.ColumnType
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructType

object DataFrameReportGenerator {

  val ReportContentName = "DataFrame Report"
  val DataSampleTableName = "Data Sample"
  val DataSchemaTableName = "Column Names and Types"
  val DataFrameSizeTableName = "DataFrame Size"
  val MaxRowsNumberInReport = 20
  val ColumnNumberToGenerateSimplerReportThreshold = 20
  val StringPreviewMaxLength = 300

  def report(df: DataFrame) =
    if (df.schema.length >= DataFrameReportGenerator.ColumnNumberToGenerateSimplerReportThreshold)
      simplifiedReport(df)
    else
      fullReport(df)

  def schemaReport(df: DataFrame) =
    Report(
      ReportContent(
        ReportContentName,
        ReportType.DataFrameSimplified,
        Seq(schemaTable(df.schema)),
        noDistributionsForSimplifiedReport(df.schema)
      )
    )

  private def fullReport(df: DataFrame) = {
    val multivarStats = calculateMultiColStats(df)
    val distributions = DistributionCalculator.distributionByColumn(df, multivarStats)
    val tables = Seq(sampleTable(df), sizeTable(df.schema, multivarStats.count))
    Report(ReportContent(ReportContentName, ReportType.DataFrameFull, tables, distributions))
  }

  private def calculateMultiColStats(df: DataFrame) =
    Statistics.colStats(df.rdd.map(SparkTypeConverter.rowToDoubleList))

  private def simplifiedReport(df: DataFrame) =
    Report(
      ReportContent(
        ReportContentName,
        ReportType.DataFrameSimplified,
        Seq(sizeTable(df.schema, df.count()), schemaTable(df.schema)),
        noDistributionsForSimplifiedReport(df.schema)
      )
    )

  private def noDistributionsForSimplifiedReport(schema: StructType): Map[String, Distribution] = {
    for (field <- schema.fields) yield field.name -> NoDistribution(field.name, NoDistributionReasons.SimplifiedReport)
  }.toMap

  private def schemaTable(schema: StructType) = {
    val values = schema.fields.zipWithIndex.map { case (field, index) =>
      val columnName = field.name
      val columnType = field.dataType.simpleString
      List(Some(index.toString), Some(columnName), Some(columnType))
    }.toList

    Table(
      DataFrameReportGenerator.DataSchemaTableName,
      s"Preview of columns and their types in dataset",
      Some(List("Column index", "Column name", "Column type")),
      List(ColumnType.Numeric, ColumnType.String, ColumnType.String),
      None,
      values
    )
  }

  private def sampleTable(df: DataFrame) = {
    val columnNames = df.schema.fieldNames.toList
    val rows = df.take(DataFrameReportGenerator.MaxRowsNumberInReport)
      .map(row =>
        columnNames.indices.map { column =>
          SparkTypeConverter.cellToString(row, column).map(ReportUtils.shortenLongStrings(_, StringPreviewMaxLength))
        }.toList
      ).toList
    val columnTypes = df.schema.map(field => SparkTypeConverter.sparkColumnTypeToColumnType(field.dataType)).toList
    Table(
      DataFrameReportGenerator.DataSampleTableName,
      s"${DataFrameReportGenerator.DataSampleTableName}. " +
        s"Randomly selected ${rows.length} rows",
      Some(columnNames),
      columnTypes,
      None,
      rows
    )
  }

  private def sizeTable(schema: StructType, rowsCount: Long) = {
    val columnsCount = schema.length
    Table(
      DataFrameReportGenerator.DataFrameSizeTableName,
      s"${DataFrameReportGenerator.DataFrameSizeTableName}. " +
        s"Number of columns and number of rows in the DataFrame.",
      Some(List("Number of columns", "Number of rows")),
      List(ColumnType.Numeric, ColumnType.Numeric),
      None,
      List(List(Some(columnsCount.toString), Some(rowsCount.toString)))
    )
  }
}
