package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters

trait GBTRegressionModelInfo extends ActionObjectInfo with PredictorParameters {

  val id = "1F71A0AA-2D2B-40C5-9004-C036D7BAEF73"

  val parameters = Left(Array(
    featuresColumnParameter,
    predictionColumnParameter
  ))
}

object GBTRegressionModelInfo extends GBTRegressionModelInfo