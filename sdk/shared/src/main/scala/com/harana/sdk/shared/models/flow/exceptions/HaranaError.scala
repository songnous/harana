package com.harana.sdk.shared.models.flow.exceptions

trait HaranaError {

  val code: FailureCode
  val title: String
  val message: String

  val details: Map[String, String] = Map()
  val id = HaranaFile.Id.randomId

  def failureDescription = FailureDescription(id, code, title, Some(message), details)

  def toException = throw new Exception(message)
}

object HaranaError {
  def throwableToDetails(cause: Option[Throwable]): Map[String, String] =
    cause.map(e => FailureDescription.stacktraceDetails(e.getStackTrace)).getOrElse(Map())
}