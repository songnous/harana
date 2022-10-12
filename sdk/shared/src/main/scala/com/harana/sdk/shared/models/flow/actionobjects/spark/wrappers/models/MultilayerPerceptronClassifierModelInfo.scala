package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

trait MultilayerPerceptronClassifierModelInfo extends ActionObjectInfo with PredictorParameters {

  val id = "FE2E65C3-CF0A-411C-926C-9A58A86B058E"

  override val parameterGroups = List(ParameterGroup("",
    featuresColumnParameter,
    predictionColumnParameter
  ))
}

object MultilayerPerceptronClassifierModelInfo extends MultilayerPerceptronClassifierModelInfo