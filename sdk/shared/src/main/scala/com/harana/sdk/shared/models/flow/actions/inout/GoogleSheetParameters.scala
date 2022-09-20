package com.harana.sdk.shared.models.flow.actions.inout

import com.harana.sdk.shared.models.flow.parameters.{Parameters, StringParameter}

trait GoogleSheetParameters { this: Parameters =>

  type GoogleSheetId = String
  type GoogleCredentialsJson = String

  val googleSheetIdParameter = StringParameter(name = "Google Sheet Id")
  def getGoogleSheetId = $(googleSheetIdParameter)
  def setGoogleSheetId(value: GoogleSheetId): this.type = set(googleSheetIdParameter, value)

  val serviceAccountCredentialsParameter = StringParameter("Google Service Account credentials JSON")
  def getGoogleServiceAccountCredentials = $(serviceAccountCredentialsParameter)
  def setGoogleServiceAccountCredentials(value: GoogleCredentialsJson): this.type = set(serviceAccountCredentialsParameter, value)

}