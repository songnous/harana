package com.harana.sdk.backend.models.flow.actions.readwritedataframe.filestorage

import com.harana.sdk.shared.models.flow.actions.inout.InputFileFormatChoice.Csv
import com.harana.sdk.backend.models.designer.flow.actions.readwritedataframe._
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.{FilePath, FilePathFromLibraryPath}
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.FileScheme.{FTP, File, HDFS, HTTP, HTTPS, Library}
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.filestorage.csv.CsvSchemaInferencerAfterReading
import com.harana.sdk.shared.models.flow.actions.inout.{InputFileFormatChoice, InputStorageTypeChoice}
import org.apache.spark.sql._

import scala.annotation.tailrec

object DataFrameFromFileReader {

  def readFromFile(fileChoice: InputStorageTypeChoice.File)(implicit context: ExecutionContext) = {
    val path = FilePath(fileChoice.getSourceFile)
    val rawDataFrame = readUsingProvidedFileScheme(path, fileChoice.getFileFormat)
    val postProcessed = fileChoice.getFileFormat match {
      case csv: Csv => CsvSchemaInferencerAfterReading.postprocess(csv)(rawDataFrame)
      case _ => rawDataFrame
    }
    postProcessed
  }

  @tailrec
  private def readUsingProvidedFileScheme(path: FilePath, fileFormat: InputFileFormatChoice)(implicit context: ExecutionContext): DataFrame =
    path.fileScheme match {
      case Library => readUsingProvidedFileScheme(FilePathFromLibraryPath(path), fileFormat)
      case File => DriverFiles.read(path.pathWithoutScheme, fileFormat)
      case HTTP | HTTPS | FTP => readUsingProvidedFileScheme(FileDownloader.downloadFile(path.fullPath), fileFormat)
      case HDFS => ClusterFiles.read(path, fileFormat)
    }
}