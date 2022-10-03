package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameter, ParameterGroup, Parameters}

trait NormalizerInfo extends TransformerInfo with Parameters {
  val id = "2921B3FE-2FEE-47D8-9F41-7DDD02F74969"

  val pParameter = DoubleParameter("p", default = Some(2.0), validator = RangeValidator(1.0, Double.PositiveInfinity))
  def getP = $(pParameter)

  val specificParameters = Array[Parameter[_]](pParameter)

}

object NormalizerInfo extends NormalizerInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}