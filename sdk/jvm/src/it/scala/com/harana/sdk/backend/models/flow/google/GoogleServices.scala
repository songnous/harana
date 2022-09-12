package com.harana.sdk.backend.models.flow.google

import java.io.File

import scala.io.Source

object GoogleServices {

  val googleSheetForTestsId = "1yllfTlFK6RkJfVxYp_hEnCikdwOuB0kc1v2XcDqANeo"

  def serviceAccountJson: Option[String] = {
    val file = new File(credentialsJsonFilePath())
    if (file.exists())
      Some(Source.fromFile(file).mkString)
    else
      None
  }

  def credentialsJsonFilePath() =
    userHomePath() + "/.credentials/harana-tests/google-service-account.json"

  private def userHomePath() = System.getProperty("user.home")

  def serviceAccountNotExistsException() = new IllegalStateException(
    s"""Google service account json does not exist.
       |Create file with google service credential json under path:
       |${GoogleServices.credentialsJsonFilePath()}""".stripMargin
  )

}
