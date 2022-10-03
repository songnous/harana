package com.harana.sdk.shared.models.flow.actiontypes.inout

import com.harana.sdk.shared.models.flow.parameters.{Parameters, StringParameter}

trait GoogleSheetParameters { this: Parameters =>

  type GoogleSheetId = String
  type GoogleCredentialsJson = String

  val googleSheetIdParameter = StringParameter("google-sheet-id")
  def getGoogleSheetId = $(googleSheetIdParameter)
  def setGoogleSheetId(value: GoogleSheetId): this.type = set(googleSheetIdParameter, value)

  val serviceAccountCredentialsParameter = StringParameter("service-account-credentials")
  def getGoogleServiceAccountCredentials = $(serviceAccountCredentialsParameter)
  def setGoogleServiceAccountCredentials(value: GoogleCredentialsJson): this.type = set(serviceAccountCredentialsParameter, value)

}