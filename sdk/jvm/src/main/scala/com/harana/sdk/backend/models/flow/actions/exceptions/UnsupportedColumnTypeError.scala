package com.harana.sdk.backend.models.flow.actions.exceptions

import com.harana.sdk.shared.models.flow.exceptions.ActionExecutionError
import io.circe.generic.JsonCodec
import org.apache.spark.sql.types.DataType

@JsonCodec
case class UnsupportedColumnTypeError(override val message: String) extends ActionExecutionError

object UnsupportedColumnTypeError {

  def apply(columnName: String, actualType: DataType): UnsupportedColumnTypeError =
    UnsupportedColumnTypeError(s"Column '$columnName' has unsupported type '${actualType.toString}'")

}
