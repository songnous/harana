package com.harana.sdk.backend.models.flow.actiontypes

import org.scalatest._
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.googlestorage._
import com.harana.sdk.backend.models.flow.{Jenkins, LocalExecutionContext, TestFiles}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.read.ReadDataFrame
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.{FilePath, FileScheme}
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.FileScheme.File
import com.harana.sdk.backend.models.flow.actiontypes.write.WriteDataFrame
import com.harana.sdk.backend.models.flow.google.GoogleServices
import com.harana.sdk.backend.models.flow.utils.{DataFrameMatchers, Logging}
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actiontypes.inout.{InputFileFormatChoice, InputStorageTypeChoice, OutputStorageTypeChoice}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class GoogleSheetSpec
    extends AnyFreeSpec
    with BeforeAndAfter
    with BeforeAndAfterAll
    with LocalExecutionContext
    with Matchers
    with TestFiles
    with Logging {

  "Harana is integrated with Google Sheets" in {

    info("It means that once given some Dataframe")
    val someDataFrame = readCsvFileFromDriver(someCsvFile)

    info("It can be saved as a Google Sheet")
    val googleSheetId = GoogleServices.googleSheetForTestsId
    writeGoogleSheet(someDataFrame, googleSheetId)

    info("And after that it can be read again from google sheet")
    val dataFrameReadAgainFromGoogleSheet = readGoogleSheet(googleSheetId)

    DataFrameMatchers.assertDataFramesEqual(dataFrameReadAgainFromGoogleSheet, someDataFrame)
  }

  private def credentials = GoogleServices.serviceAccountJson match {
    case Some(credentials) => credentials
  }

  private def writeGoogleSheet(dataframe: DataFrame, googleSheetId: GoogleSheetId) = {
    val write = new WriteDataFrame()
      .setStorageType(
        new OutputStorageTypeChoice.GoogleSheet().setGoogleServiceAccountCredentials(credentials).setGoogleSheetId(googleSheetId)
      )
    write.executeUntyped(List(dataframe))(executionContext)
  }

  private def readGoogleSheet(googleSheetId: GoogleSheetId) = {
    val readDF = new ReadDataFrame()
      .setStorageType(
        new InputStorageTypeChoice.GoogleSheet().setGoogleSheetId(googleSheetId).setGoogleServiceAccountCredentials(credentials)
      )
    readDF.executeUntyped(List.empty[ActionObjectInfo])(executionContext).head
  }

  private def readCsvFileFromDriver(filePath: FilePath) = {
    require(filePath.fileScheme == File)
    val readDF = new ReadDataFrame()
      .setStorageType(
        new InputStorageTypeChoice.File()
          .setSourceFile(filePath.fullPath).setFileFormat(new InputFileFormatChoice.Csv().setNamesIncluded(true))
      )
    readDF.executeUntyped(List.empty[ActionObjectInfo])(executionContext).head
  }
}
