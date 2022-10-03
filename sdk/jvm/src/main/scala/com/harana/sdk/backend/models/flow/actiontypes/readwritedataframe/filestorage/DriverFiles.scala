package com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.filestorage

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.{FilePath, FileScheme}
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.filestorage.csv.CsvOptions
import com.harana.sdk.backend.models.flow.utils.ManagedResource
import com.harana.sdk.shared.models.flow.actiontypes.inout.{InputFileFormatChoice, OutputFileFormatChoice}
import com.harana.spark.SQL
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.execution.datasources.csv.{DataframeToDriverCsvFileWriter, RawCsvRDDToDataframe}
import org.apache.spark.sql.{Encoders, SaveMode, DataFrame => SparkDataFrame}

import java.io.{File, IOException, PrintWriter}
import scala.io.Source

object DriverFiles {

  def read(driverPath: String, fileFormat: InputFileFormatChoice)(implicit context: ExecutionContext): SparkDataFrame =
    fileFormat match {
      case csv: InputFileFormatChoice.Csv         => readCsv(driverPath, csv)
      case json: InputFileFormatChoice.Json       => readJson(driverPath)
      case parquet: InputFileFormatChoice.Parquet => throw ParquetNotSupported().toException
    }

  def write(dataFrame: DataFrame, path: FilePath, fileFormat: OutputFileFormatChoice, saveMode: SaveMode)(implicit context: ExecutionContext) = {
    path.verifyScheme(FileScheme.File)
    if (saveMode == SaveMode.ErrorIfExists && new File(path.pathWithoutScheme).exists()) throw new IOException(s"Output file ${path.fullPath} already exists")
    fileFormat match {
      case csv: OutputFileFormatChoice.Csv         => writeCsv(path, csv, dataFrame)
      case json: OutputFileFormatChoice.Json       => writeJson(path, dataFrame)
      case parquet: OutputFileFormatChoice.Parquet => throw ParquetNotSupported().toException
    }
  }

  private def readCsv(driverPath: String, csvChoice: InputFileFormatChoice.Csv)(implicit context: ExecutionContext) = {
    val parameters = CsvOptions.map(csvChoice.getNamesIncluded, csvChoice.getCsvColumnSeparator)
    val lines = Source.fromFile(driverPath).getLines().toIndexedSeq
    val fileLinesRdd = context.sparkContext.parallelize(lines)

    RawCsvRDDToDataframe.parse(fileLinesRdd, context.sparkSQLSession.sparkSession, parameters)
  }

  private def readJson(driverPath: String)(implicit context: ExecutionContext) = {
    val lines = Source.fromFile(driverPath).getLines().toSeq
    val fileLinesRdd = context.sparkContext.parallelize(lines)
    val sparkSession = context.sparkSQLSession.sparkSession
    val dataset = sparkSession.createDataset(fileLinesRdd)(Encoders.STRING)
    sparkSession.read.json(dataset)
  }

  private def writeCsv(path: FilePath, csvChoice: OutputFileFormatChoice.Csv, dataFrame: DataFrame)(implicit context: ExecutionContext) = {
    val parameters = CsvOptions.map(csvChoice.getNamesIncluded, csvChoice.getCsvColumnSeparator)

    DataframeToDriverCsvFileWriter.write(
      dataFrame.sparkDataFrame,
      parameters,
      dataFrame.schema.get,
      path.pathWithoutScheme,
      context.sparkSQLSession.sparkSession
    )
  }

  private def writeJson(path: FilePath, dataFrame: DataFrame)(implicit context: ExecutionContext) = {
    val rawJsonLines: RDD[String] = SQL.dataFrameToJsonRDD(dataFrame.sparkDataFrame)
    writeRddToDriverFile(path.pathWithoutScheme, rawJsonLines)
  }

  private def writeRddToDriverFile(driverPath: String, lines: RDD[String]) = {
    val recordSeparator = System.getProperty("line.separator", "\n")
    ManagedResource(new PrintWriter(driverPath)) { writer =>
      lines.collect().foreach(line => writer.write(line + recordSeparator))
    }
  }
}
