package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.AFTSurvivalRegressionParameters
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

trait AFTSurvivalRegressionModelInfo
    extends ActionObjectInfo
    with PredictorParameters
    with AFTSurvivalRegressionParameters {

  val id = "E8112371-B8BB-4EF1-A4A5-E02073B00531"

  override val parameterGroups = List(ParameterGroup("",
    featuresColumnParameter,
    predictionColumnParameter,
    quantileProbabilitiesParameter,
    optionalQuantilesColumnParameter
  ))

}

object AFTSurvivalRegressionModelInfo extends AFTSurvivalRegressionModelInfo