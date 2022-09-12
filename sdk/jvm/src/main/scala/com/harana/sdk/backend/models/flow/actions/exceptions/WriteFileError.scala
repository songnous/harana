package com.harana.sdk.backend.models.flow.actions.exceptions

import com.harana.sdk.shared.models.flow.exceptions.{FailureCode, HaranaError}
import io.circe.generic.JsonCodec
import org.apache.spark.SparkException

@JsonCodec
case class WriteFileError(path: String, override val details: Map[String, String] = Map()) extends HaranaError {
  val code = FailureCode.NodeFailure
  val title = "WriteFileException"
  val message = s"Unable to write file: $path"
}