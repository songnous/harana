package com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.googlestorage

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.model.File
import com.google.api.services.drive.{Drive, DriveScopes}
import com.harana.sdk.shared.models.flow.actiontypes.inout.CsvParameters.ColumnSeparatorChoice
import com.harana.sdk.backend.models.flow.utils.{LoggerForCallerClass, ManagedResource}

import java.io.{ByteArrayInputStream, FileOutputStream}
import java.util

object GoogleDriveClient {

  val logger = LoggerForCallerClass()

  val googleSheetCsvSeparator = ColumnSeparatorChoice.Comma()

  private val ApplicationName = "Harana"

  private val Scopes = util.Arrays.asList(DriveScopes.DRIVE)

  def uploadCsvFileAsGoogleSheet(credentials: GoogleCredentialsJson, sheetId: GoogleSheetId, filePath: String) = {
    val fileMetadata = new File().setMimeType("application/vnd.google-apps.spreadsheet")
    val mediaContent = new FileContent("text/csv", new java.io.File(filePath))

    driveService(credentials).files.update(sheetId, fileMetadata, mediaContent).execute
  }

  def downloadGoogleSheetAsCsvFile(credentials: GoogleCredentialsJson, sheetId: GoogleSheetId, filePath: String) = {
    val file = new java.io.File(filePath)
    file.getParentFile.mkdirs()

    ManagedResource(new FileOutputStream(file)) { fos =>
      driveService(credentials).files().export(sheetId, "text/csv").executeMediaAndDownloadTo(fos)
      logger.info(s"Downloaded google sheet id=$sheetId to the file $filePath")
    }
  }

  private def driveService(serviceAccountCredentials: String) = {
    val credential = {
      val in = new ByteArrayInputStream(serviceAccountCredentials.getBytes)
      GoogleCredential.fromStream(in).createScoped(Scopes)
    }
    new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential).setApplicationName(ApplicationName).build
  }

  private val jsonFactory = GsonFactory.getDefaultInstance

}
