package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, IntParameter, Parameter, Parameters, StringParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

trait RegexTokenizerInfo extends TransformerInfo with Parameters {

  val id = "DCD2E3C2-39A1-4289-A16D-4B4958C9264D"

  val gapsParameter = BooleanParameter("gaps", Some("Indicates whether the regex splits on gaps (true) or matches tokens (false)."))

  setDefault(gapsParameter, true)

  val minTokenLengthParameter = IntParameter("min token length", Some("The minimum token length."),
    validator = RangeValidator.positiveIntegers
  )

  setDefault(minTokenLengthParameter, 1)

  val patternParameter = StringParameter(
    name = "pattern",
    description = Some("""The regex pattern used to match delimiters (gaps = true) or tokens
                         |(gaps = false).""".stripMargin),
  )

  setDefault(patternParameter, "\\s+")

  val specificParameters = Array[Parameter[_]](gapsParameter, minTokenLengthParameter, patternParameter)

}

object RegexTokenizerInfo extends RegexTokenizerInfo {
  val parameters = Array.empty
}