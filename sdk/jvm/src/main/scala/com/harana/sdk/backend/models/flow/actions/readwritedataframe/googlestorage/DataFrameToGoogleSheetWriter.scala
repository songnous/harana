package com.harana.sdk.backend.models.flow.actions.readwritedataframe.googlestorage

import com.harana.sdk.shared.models.flow.actions.inout.OutputStorageTypeChoice.GoogleSheet
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.{FilePath, FileScheme}
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.filestorage.DataFrameToFileWriter
import com.harana.sdk.backend.models.flow.utils.{FileOperations, LoggerForCallerClass}
import com.harana.sdk.shared.models.flow.actions.inout.{OutputFileFormatChoice, OutputStorageTypeChoice}

import java.util.UUID

object DataFrameToGoogleSheetWriter {

  val logger = LoggerForCallerClass()

  def writeToGoogleSheet(googleSheetChoice: GoogleSheet, context: ExecutionContext, dataFrame: DataFrame) = {
    val localTmpFile: FilePath = saveDataFrameAsDriverCsvFile(googleSheetChoice, context, dataFrame)
    GoogleDriveClient.uploadCsvFileAsGoogleSheet(googleSheetChoice.getGoogleServiceAccountCredentials, googleSheetChoice.getGoogleSheetId, localTmpFile.pathWithoutScheme)
  }

  private def saveDataFrameAsDriverCsvFile(googleSheetChoice: GoogleSheet, context: ExecutionContext, dataFrame: DataFrame): FilePath = {
    val sheetId = googleSheetChoice.getGoogleSheetId
    val localTmpFile = FilePath(FileScheme.File, s"/tmp/harana/google_sheet_${sheetId}__${UUID.randomUUID()}.csv")

    FileOperations.mkdirsParents(new java.io.File(localTmpFile.pathWithoutScheme))

    val localTmpFileParameters = new OutputStorageTypeChoice.File()
      .setOutputFile(localTmpFile.fullPath)
      .setFileFormat(
        new OutputFileFormatChoice.Csv()
          .setCsvColumnSeparator(GoogleDriveClient.googleSheetCsvSeparator)
          .setNamesIncluded(googleSheetChoice.getNamesIncluded)
      )
    DataFrameToFileWriter.writeToFile(localTmpFileParameters, context, dataFrame)
    localTmpFile
  }
}
