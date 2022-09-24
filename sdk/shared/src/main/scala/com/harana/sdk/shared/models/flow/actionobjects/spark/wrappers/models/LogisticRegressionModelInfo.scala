package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasThresholdParameter, ProbabilisticClassifierParameters}

trait LogisticRegressionModelInfo extends ActionObjectInfo with ProbabilisticClassifierParameters with HasThresholdParameter {

  val id = "FC963E60-2DBA-4504-B6C7-27B53F7825F0"

  val parameters = Left(List(
    featuresColumnParameter,
    probabilityColumnParameter,
    rawPredictionColumnParameter,
    predictionColumnParameter,
    thresholdParameter
  ))
}

object LogisticRegressionModelInfo extends LogisticRegressionModelInfo