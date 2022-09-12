package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.SparkModelWrapperInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.ProbabilisticClassifierParameters
import com.harana.sdk.shared.models.flow.actionobjects.stringindexingwrapper.StringIndexingWrapperModelInfo
import com.harana.sdk.shared.models.flow.parameters.Parameter

class RandomForestClassificationModelInfo(model: VanillaRandomForestClassificationModelInfo = new VanillaRandomForestClassificationModelInfo() {})
    extends StringIndexingWrapperModelInfo(model) {

  val id = "5E45320A-D21C-4AC0-925C-82AED31B83D3"
}

trait VanillaRandomForestClassificationModelInfo extends SparkModelWrapperInfo with ProbabilisticClassifierParameters {

  val id = "94CD2E91-C1E4-49D1-A1FD-6BF110628D1E"

  val parameters = Array.empty[Parameter[_]]

  val specificParameters = Array(
    featuresColumnParameter,
    predictionColumnParameter,
    probabilityColumnParameter,
    rawPredictionColumnParameter
  )
}

object VanillaRandomForestClassificationModelInfo extends VanillaRandomForestClassificationModelInfo