package com.harana.sdk.shared.models.flow.exceptions

import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.generic.JsonCodec

@JsonCodec
case class FailureDescription(id: Id,
                              code: FailureCode,
                              title: String,
                              message: Option[String] = None,
                              details: Map[String, String] = Map()
)

object FailureDescription {

  def stacktraceDetails(stackTrace: Array[StackTraceElement]): Map[String, String] = {
    val ls = java.lang.System.lineSeparator()
    Map("stacktrace" -> stackTrace.mkString("", ls, ls))
  }
}
