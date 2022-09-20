package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

trait NGramTransformerInfo extends TransformerInfo with Parameters {

  val id = "48C17F3A-A8C3-46B3-96A4-A4792809841B"

  val nParameter = IntParameter("n", validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))
  setDefault(nParameter, 2)

  val specificParameters = Array[Parameter[_]](nParameter)

  def setN(value: Int): this.type = set(nParameter -> value)

}

object NGramTransformerInfo extends NGramTransformerInfo {
  val parameters = Array.empty
}