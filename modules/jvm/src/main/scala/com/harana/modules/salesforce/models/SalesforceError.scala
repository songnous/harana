package com.harana.modules.salesforce.models

import com.harana.modules.core.okhttp.models.OkHttpError

sealed trait SalesforceError
object SalesforceError {
  case object ParseError extends SalesforceError
  case class ConnectionError(err: OkHttpError) extends SalesforceError
}