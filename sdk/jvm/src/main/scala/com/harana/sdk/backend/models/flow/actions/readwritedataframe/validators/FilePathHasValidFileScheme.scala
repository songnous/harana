package com.harana.sdk.backend.models.flow.actions.readwritedataframe.validators

import com.harana.sdk.backend.models.flow.actions.read.ReadDataFrame
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.FilePath
import com.harana.sdk.backend.models.flow.actions.write.WriteDataFrame
import com.harana.sdk.shared.models.flow.actions.inout.{InputStorageTypeChoice, OutputStorageTypeChoice}

object FilePathHasValidFileScheme {

  def validate(wdf: WriteDataFrame) = {
    import OutputStorageTypeChoice._

    wdf.getStorageType match {
      case file: File =>
        val path = file.getOutputFile
        FilePath(path)
      case _          =>
    }
  }

  def validate(rdf: ReadDataFrame) = {
    import InputStorageTypeChoice._

    rdf.getStorageType match {
      case file: File =>
        val path = file.getSourceFile
        FilePath(path)
      case _          =>
    }
  }
}
