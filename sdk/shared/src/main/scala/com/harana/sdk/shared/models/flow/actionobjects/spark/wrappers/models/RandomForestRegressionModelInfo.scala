package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters

trait RandomForestRegressionModelInfo extends ActionObjectInfo with PredictorParameters {

  val id = "1526C59C-D6E3-4AA8-AA8A-CE1CF30DB909"

  val parameters = Left(Array(
    featuresColumnParameter,
    predictionColumnParameter
  ))
}

object RandomForestRegressionModelInfo extends RandomForestRegressionModelInfo