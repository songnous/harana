package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters

trait MultilayerPerceptronClassifierModelInfo extends ActionObjectInfo with PredictorParameters {

  val id = "FE2E65C3-CF0A-411C-926C-9A58A86B058E"

  val parameters = Left(Array(
    featuresColumnParameter,
    predictionColumnParameter
  ))
}

object MultilayerPerceptronClassifierModelInfo extends MultilayerPerceptronClassifierModelInfo