package com.harana.sdk.shared.models.flow.exceptions

import com.harana.sdk.shared.models.common.Version

trait WorkflowVersionError extends HaranaError {
  val code = FailureCode.IncorrectWorkflow
}

case class WorkflowVersionFormatError(stringVersion: String) extends WorkflowVersionError {
  val title = "Workflow's version has wrong format"
  val message = s"Got '$stringVersion' but expected X.Y.Z where X, Y, Z are positive integers"
}

case class WorkflowVersionNotFoundError(supportedApiVersion: Version) extends WorkflowVersionError {
  val title = "API version was not included in the request"
  val message = s"Currently supported version is ${supportedApiVersion.humanReadable}"
  override val details = Map("supportedApiVersion" -> supportedApiVersion.humanReadable)
}

case class WorkflowVersionNotSupportedError(workflowApiVersion: Version, supportedApiVersion: Version) extends WorkflowVersionError {
  val title = s"API version ${workflowApiVersion.humanReadable} is not supported"
  val message = s"Currently supported version is ${supportedApiVersion.humanReadable}"
  override val details = Map(
    "workflowApiVersion" -> workflowApiVersion.humanReadable,
    "supportedApiVersion" -> supportedApiVersion.humanReadable
  )
}