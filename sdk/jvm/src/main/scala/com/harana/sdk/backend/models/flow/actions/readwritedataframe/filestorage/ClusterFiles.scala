package com.harana.sdk.backend.models.flow.actions.readwritedataframe.filestorage

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow.actions.inout.OutputFileFormatChoice.Csv
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.FilePath
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.filestorage.csv.CsvOptions
import com.harana.sdk.shared.models.flow.actions.inout.{InputFileFormatChoice, OutputFileFormatChoice}
import org.apache.spark.sql.{SaveMode, DataFrame => SparkDataFrame}

object ClusterFiles {

  import com.harana.sdk.backend.models.flow.actions.readwritedataframe.filestorage.csv.CsvOptions._

  def read(path: FilePath, fileFormat: InputFileFormatChoice)(implicit context: ExecutionContext): SparkDataFrame = {
    val clusterPath = path.fullPath
    fileFormat match {
      case csv: InputFileFormatChoice.Csv => readCsv(clusterPath, csv)
      case _: InputFileFormatChoice.Json => context.sparkSQLSession.read.json(clusterPath)
      case _: InputFileFormatChoice.Parquet => context.sparkSQLSession.read.parquet(clusterPath)
    }
  }

  def write(dataFrame: DataFrame, path: FilePath, fileFormat: OutputFileFormatChoice, saveMode: SaveMode)(implicit context: ExecutionContext) = {
    val clusterPath = path.fullPath
    val writer = fileFormat match {
      case csvChoice: Csv =>
        val namesIncluded = csvChoice.getNamesIncluded
        dataFrame.sparkDataFrame.write.format("com.databricks.spark.csv").options(CsvOptions.map(namesIncluded, csvChoice.getCsvColumnSeparator))
      case _: OutputFileFormatChoice.Parquet => dataFrame.sparkDataFrame.write.format("parquet")
      case _: OutputFileFormatChoice.Json    => dataFrame.sparkDataFrame.write.format("json")
    }
    writer.mode(saveMode).save(clusterPath)
  }

  private def readCsv(clusterPath: String, csvChoice: InputFileFormatChoice.Csv)(implicit context: ExecutionContext) =
    context.sparkSQLSession.read
      .format("com.databricks.spark.csv")
      .setCsvOptions(csvChoice.getNamesIncluded, csvChoice.getCsvColumnSeparator)
      .load(clusterPath)

}
