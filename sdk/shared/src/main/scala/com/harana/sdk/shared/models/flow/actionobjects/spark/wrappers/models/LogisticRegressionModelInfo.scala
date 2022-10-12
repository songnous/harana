package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasThresholdParameter, ProbabilisticClassifierParameters}
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

trait LogisticRegressionModelInfo extends ActionObjectInfo with ProbabilisticClassifierParameters with HasThresholdParameter {

  val id = "FC963E60-2DBA-4504-B6C7-27B53F7825F0"

  override val parameterGroups = List(ParameterGroup("",
    featuresColumnParameter,
    probabilityColumnParameter,
    rawPredictionColumnParameter,
    predictionColumnParameter,
    thresholdParameter
  ))
}

object LogisticRegressionModelInfo extends LogisticRegressionModelInfo