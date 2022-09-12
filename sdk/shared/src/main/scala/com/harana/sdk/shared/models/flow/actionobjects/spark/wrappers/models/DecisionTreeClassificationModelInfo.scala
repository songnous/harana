package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.SparkModelWrapperInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.ProbabilisticClassifierParameters
import com.harana.sdk.shared.models.flow.actionobjects.stringindexingwrapper.StringIndexingWrapperModelInfo

class DecisionTreeClassificationModelInfo(vanillaModel: VanillaDecisionTreeClassificationModelInfo)
  extends StringIndexingWrapperModelInfo(vanillaModel) {

  val id = "60290377-6A93-495B-BC3E-14F7159B7791"

  def this() = this(new VanillaDecisionTreeClassificationModelInfo{})
}

trait VanillaDecisionTreeClassificationModelInfo extends SparkModelWrapperInfo with ProbabilisticClassifierParameters {
  val id = "90FACCEF-6B9C-46E2-9594-D8A3C7A040F5"
  val parameters = Array(featuresColumnParameter, probabilityColumnParameter, rawPredictionColumnParameter, predictionColumnParameter)
}