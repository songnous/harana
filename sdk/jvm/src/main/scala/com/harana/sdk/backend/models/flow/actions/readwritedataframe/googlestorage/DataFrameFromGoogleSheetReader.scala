package com.harana.sdk.backend.models.flow.actions.readwritedataframe.googlestorage

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.{FilePath, FileScheme}
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.filestorage.DataFrameFromFileReader
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.flow.actions.inout.{InputFileFormatChoice, InputStorageTypeChoice}
import org.apache.spark.sql._

import java.io.{File => _}
import java.util.UUID

object DataFrameFromGoogleSheetReader extends Logging {

  def readFromGoogleSheet(googleSheet: InputStorageTypeChoice.GoogleSheet)(implicit context: ExecutionContext) = {
    val id = googleSheet.getGoogleSheetId
    val tmpPath = FilePath(FileScheme.File, s"/tmp/harana/google_sheet_${id}__${UUID.randomUUID()}.csv")

    GoogleDriveClient.downloadGoogleSheetAsCsvFile(
      googleSheet.getGoogleServiceAccountCredentials,
      googleSheet.getGoogleSheetId,
      tmpPath.pathWithoutScheme
    )

    val readDownloadedGoogleFileParameters = new InputStorageTypeChoice.File()
      .setFileFormat(
        new InputFileFormatChoice.Csv()
          .setCsvColumnSeparator(GoogleDriveClient.googleSheetCsvSeparator)
          .setShouldConvertToBoolean(googleSheet.getShouldConvertToBoolean)
          .setNamesIncluded(googleSheet.getNamesIncluded)
      )
      .setSourceFile(tmpPath.fullPath)

    DataFrameFromFileReader.readFromFile(readDownloadedGoogleFileParameters)
  }
}
