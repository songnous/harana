package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, IntParameter, Parameter, Parameters, StringParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

trait RegexTokenizerInfo extends TransformerInfo with Parameters {

  val id = "DCD2E3C2-39A1-4289-A16D-4B4958C9264D"

  val gapsParameter = BooleanParameter("gaps", default = Some(true))
  val minTokenLengthParameter = IntParameter("min token length", default = Some(1), validator = RangeValidator.positiveIntegers)

  val patternParameter = StringParameter("pattern", default = Some("\\s+"))

  val specificParameters = Array[Parameter[_]](gapsParameter, minTokenLengthParameter, patternParameter)

}

object RegexTokenizerInfo extends RegexTokenizerInfo {
  val parameters = Left(Array.empty[Parameter[_]])
}