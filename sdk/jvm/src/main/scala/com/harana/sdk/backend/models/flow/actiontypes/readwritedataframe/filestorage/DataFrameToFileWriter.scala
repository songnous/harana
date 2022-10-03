package com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.filestorage

import com.harana.sdk.shared.models.flow.actiontypes.inout.OutputFileFormatChoice.Csv
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.WriteFileError
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.{FilePath, FilePathFromLibraryPath, FileScheme}
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.filestorage.csv.CsvSchemaStringifierBeforeCsvWriting
import com.harana.sdk.backend.models.flow.filesystemclients.FileSystemClient
import com.harana.sdk.backend.models.flow.utils.LoggerForCallerClass
import com.harana.sdk.shared.models.flow.actiontypes.inout.OutputStorageTypeChoice
import com.harana.sdk.shared.models.flow.exceptions.{FlowError, HaranaError}
import org.apache.spark.SparkException
import org.apache.spark.sql.SaveMode

import scala.annotation.tailrec

object DataFrameToFileWriter {

  val logger = LoggerForCallerClass()

  def writeToFile(fileChoice: OutputStorageTypeChoice.File, context: ExecutionContext, dataFrame: DataFrame) = {
    implicit val ctx = context

    val path = FileSystemClient.replaceLeadingTildeWithHomeDirectory(fileChoice.getOutputFile)
    val filePath = FilePath(path)
    val saveMode = if (fileChoice.getShouldOverwrite) SaveMode.Overwrite else SaveMode.ErrorIfExists

    try {
      val preprocessed = fileChoice.getFileFormat match {
        case _: Csv => CsvSchemaStringifierBeforeCsvWriting.preprocess(dataFrame)
        case _ => dataFrame
      }
      writeUsingProvidedFileScheme(fileChoice, preprocessed, filePath, saveMode)
    } catch {
      case e: SparkException =>
        logger.error(s"WriteDataFrame error: Spark problem. Unable to write file to $path", e)
        throw WriteFileError(path, HaranaError.throwableToDetails(Some(e))).toException
    }
  }

  @tailrec
  private def writeUsingProvidedFileScheme(fileChoice: OutputStorageTypeChoice.File, df: DataFrame, path: FilePath, saveMode: SaveMode)(implicit context: ExecutionContext): Unit = {
    import FileScheme._
    path.fileScheme match {
      case Library =>
        val filePath = FilePathFromLibraryPath(path)
        val FilePath(_, libraryPath) = filePath
        new java.io.File(libraryPath).getParentFile.mkdirs()
        writeUsingProvidedFileScheme(fileChoice, df, filePath, saveMode)
      case FileScheme.File => DriverFiles.write(df, path, fileChoice.getFileFormat, saveMode)
      case HDFS => ClusterFiles.write(df, path, fileChoice.getFileFormat, saveMode)
      case HTTP | HTTPS | FTP => throw NotSupportedScheme(path.fileScheme).toException
    }
  }

  case class NotSupportedScheme(fileScheme: FileScheme) extends FlowError {
    val message = s"Not supported file scheme ${fileScheme.pathPrefix}"
  }
}