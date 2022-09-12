package com.harana.workflowexecutor.exception

import akka.http.scaladsl.model.StatusCode

case class UnexpectedHttpResponseException(message: String, statusCode: StatusCode, content: String)
    extends Exception(s"$message: Unexpected HTTP response: $statusCode")
