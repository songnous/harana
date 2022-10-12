package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

trait RandomForestRegressionModelInfo extends ActionObjectInfo with PredictorParameters {

  val id = "1526C59C-D6E3-4AA8-AA8A-CE1CF30DB909"

  override val parameterGroups = List(ParameterGroup("",
    featuresColumnParameter,
    predictionColumnParameter
  ))
}

object RandomForestRegressionModelInfo extends RandomForestRegressionModelInfo