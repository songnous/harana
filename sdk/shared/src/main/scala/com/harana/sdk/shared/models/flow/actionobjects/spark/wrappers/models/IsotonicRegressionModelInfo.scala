package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasFeatureIndexParameter, PredictorParameters}
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

trait IsotonicRegressionModelInfo extends ActionObjectInfo with PredictorParameters with HasFeatureIndexParameter {

  val id = "328370B5-5C44-43F8-8840-A1904DA9E44A"

  override val parameterGroups = List(ParameterGroup("",
    featureIndexParameter,
    featuresColumnParameter,
    predictionColumnParameter
  ))
}

object IsotonicRegressionModelInfo extends IsotonicRegressionModelInfo