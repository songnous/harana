package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

trait HashingTFTransformerInfo extends TransformerInfo with Parameters {

  val id = "172CB72E-4A2D-4C19-861C-657CED88D9B0"

  val numFeaturesParameter = IntParameter("num features", Some("The number of features."),
    validator = RangeValidator(1, Int.MaxValue, step = Some(1))
  )

  // With default setting in Bundled Image (1 << 20) makes jvm run out of memory even for few rows.
  setDefault(numFeaturesParameter, (1 << 18))

  val specificParameters = Array[Parameter[_]](numFeaturesParameter)

  def setNumFeatures(value: Int): this.type = set(numFeaturesParameter -> value)

}

object HashingTFTransformerInfo extends HashingTFTransformerInfo {
  val parameters = Array.empty
}