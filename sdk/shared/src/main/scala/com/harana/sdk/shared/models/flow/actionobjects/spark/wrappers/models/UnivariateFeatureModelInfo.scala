package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasFeaturesColumnParameter, HasLabelColumnParameter, HasOutputColumnParameter}

trait UnivariateFeatureModelInfo
  extends TransformerInfo
    with HasFeaturesColumnParameter
    with HasOutputColumnParameter
    with HasLabelColumnParameter {

  val id = "9B42F09E-F4D6-4F29-8C27-53D96A470C9D"

  val parameters = Left(Array(
    featuresColumnParameter,
    outputColumnParameter,
    labelColumnParameter
  ))
}

object UnivariateFeatureModelInfo extends UnivariateFeatureModelInfo