package com.harana.sdk.backend.models.flow.actiontypes.exceptions

import com.harana.sdk.shared.models.flow.exceptions.ActionExecutionError
import org.apache.spark.sql.types.StructField

case class ValueConversionError(value: String, field: StructField) extends ActionExecutionError {
  val message = "Value \"" + value + "\" can't be converted to a column \"" + field.name + "\" " + "type \"" + field.dataType.simpleString + "\""
}