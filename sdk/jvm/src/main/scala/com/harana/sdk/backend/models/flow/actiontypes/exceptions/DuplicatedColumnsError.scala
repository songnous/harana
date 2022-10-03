package com.harana.sdk.backend.models.flow.actiontypes.exceptions

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import io.circe.generic.JsonCodec

@JsonCodec
case class DuplicatedColumnsError(columns: List[String]) extends FlowError {
  val message = s"""|DataFrame contains duplicated column names: |${columns.map(col => s"`$col`").mkString("[", ", ", "]")}""".stripMargin
}