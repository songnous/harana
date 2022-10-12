package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

trait LinearRegressionModelInfo extends ActionObjectInfo with PredictorParameters {

  val id = "9258942A-755E-4916-84DF-4F2B12C9A858"

  override val parameterGroups = List(ParameterGroup("",
    featuresColumnParameter,
    predictionColumnParameter
  ))

}

object LinearRegressionModelInfo extends LinearRegressionModelInfo