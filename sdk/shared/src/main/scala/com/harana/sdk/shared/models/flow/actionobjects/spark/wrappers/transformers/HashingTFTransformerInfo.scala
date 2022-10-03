package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter, ParameterGroup, Parameters}

trait HashingTFTransformerInfo extends TransformerInfo with Parameters {

  val id = "172CB72E-4A2D-4C19-861C-657CED88D9B0"

  val numFeaturesParameter = IntParameter("num-features", default = Some(1 << 18), validator = RangeValidator(1, Int.MaxValue, step = Some(1)))
  def setNumFeatures(value: Int): this.type = set(numFeaturesParameter -> value)

  val specificParameters = Array[Parameter[_]](numFeaturesParameter)

}

object HashingTFTransformerInfo extends HashingTFTransformerInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}