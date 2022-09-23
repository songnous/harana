package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

trait HashingTFTransformerInfo extends TransformerInfo with Parameters {

  val id = "172CB72E-4A2D-4C19-861C-657CED88D9B0"

  val numFeaturesParameter = IntParameter("num features", validator = RangeValidator(1, Int.MaxValue, step = Some(1)))
  setDefault(numFeaturesParameter, (1 << 18))
  def setNumFeatures(value: Int): this.type = set(numFeaturesParameter -> value)

  val specificParameters = Array[Parameter[_]](numFeaturesParameter)

}

object HashingTFTransformerInfo extends HashingTFTransformerInfo {
  val parameters = Left(Array.empty[Parameter[_]])
}