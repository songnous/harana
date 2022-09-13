package com.harana.sdk.backend.models.flow.actions.readwritedataframe.validators

import com.harana.sdk.backend.models.flow.actions.read.ReadDataFrame
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.{FilePath, FileScheme}
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.filestorage.ParquetNotSupported
import com.harana.sdk.backend.models.flow.actions.write.WriteDataFrame
import com.harana.sdk.shared.models.flow.actions.inout.{InputFileFormatChoice, InputStorageTypeChoice, OutputFileFormatChoice, OutputStorageTypeChoice}

object ParquetSupportedOnClusterOnly {

  def validate(wdf: WriteDataFrame) = {
    import OutputFileFormatChoice._
    import OutputStorageTypeChoice._

    wdf.getStorageType match {
      case file: File =>
        file.getFileFormat match {
          case _: Parquet =>
            val path       = file.getOutputFile
            val filePath   = FilePath(path)
            val fileScheme = filePath.fileScheme
            if (!FileScheme.supportedByParquet.contains(fileScheme))
              throw ParquetNotSupported().toException
          case _          =>
        }
      case _          =>
    }
  }

  def validate(rdf: ReadDataFrame) = {
    import InputFileFormatChoice._
    import InputStorageTypeChoice._

    rdf.getStorageType match {
      case file: File =>
        file.getFileFormat match {
          case _: Parquet =>
            val path       = file.getSourceFile
            val filePath   = FilePath(path)
            val fileScheme = filePath.fileScheme
            if (!FileScheme.supportedByParquet.contains(fileScheme)) throw ParquetNotSupported().toException
          case _          =>
        }
      case _          =>
    }
  }
}
