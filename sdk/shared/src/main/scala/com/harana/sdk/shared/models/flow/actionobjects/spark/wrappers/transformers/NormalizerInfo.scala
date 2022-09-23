package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

trait NormalizerInfo extends TransformerInfo with Parameters {
  val id = "2921B3FE-2FEE-47D8-9F41-7DDD02F74969"

  val pParameter = DoubleParameter("p", validator = RangeValidator(1.0, Double.PositiveInfinity))
  def getP = $(pParameter)
  setDefault(pParameter, 2.0)

  val specificParameters = Array[Parameter[_]](pParameter)

}

object NormalizerInfo extends NormalizerInfo {
  val parameters = Left(Array.empty[Parameter[_]])
}