package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.ProbabilisticClassifierParameters
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

trait NaiveBayesModelInfo extends ActionObjectInfo with ProbabilisticClassifierParameters {

  val id = "3ACD4481-B305-45BD-9A63-5111D6CC457D"

  val parameterGroups = List(ParameterGroup(None,
    featuresColumnParameter,
    probabilityColumnParameter,
    rawPredictionColumnParameter,
    predictionColumnParameter
  ))
}

object NaiveBayesModelInfo extends NaiveBayesModelInfo