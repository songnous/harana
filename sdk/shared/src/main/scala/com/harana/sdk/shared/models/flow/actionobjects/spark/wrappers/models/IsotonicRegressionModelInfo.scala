package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasFeatureIndexParameter, PredictorParameters}

trait IsotonicRegressionModelInfo extends ActionObjectInfo with PredictorParameters with HasFeatureIndexParameter {

  val id = "328370B5-5C44-43F8-8840-A1904DA9E44A"

  val parameters = Left(List(
    featureIndexParameter,
    featuresColumnParameter,
    predictionColumnParameter
  ))
}

object IsotonicRegressionModelInfo extends IsotonicRegressionModelInfo