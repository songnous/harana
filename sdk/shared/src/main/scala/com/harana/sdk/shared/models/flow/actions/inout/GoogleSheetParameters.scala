package com.harana.sdk.shared.models.flow.actions.inout

import com.harana.sdk.shared.models.flow.parameters.{Parameters, StringParameter}

trait GoogleSheetParameters { this: Parameters =>

  type GoogleSheetId = String
  type GoogleCredentialsJson = String

  val googleSheetIdParameter = StringParameter(name = "Google Sheet Id", description = None)
  def getGoogleSheetId = $(googleSheetIdParameter)
  def setGoogleSheetId(value: GoogleSheetId): this.type = set(googleSheetIdParameter, value)

  val serviceAccountCredentialsParameter = StringParameter("Google Service Account credentials JSON", Some("Json file representing google service account credentials to be used for accessing Google sheet."))
  def getGoogleServiceAccountCredentials = $(serviceAccountCredentialsParameter)
  def setGoogleServiceAccountCredentials(value: GoogleCredentialsJson): this.type = set(serviceAccountCredentialsParameter, value)

}