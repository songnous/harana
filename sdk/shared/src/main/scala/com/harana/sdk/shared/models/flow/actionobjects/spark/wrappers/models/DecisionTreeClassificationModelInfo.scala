package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.ProbabilisticClassifierParameters
import com.harana.sdk.shared.models.flow.actionobjects.{SparkModelWrapperInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, ParameterMap}

trait DecisionTreeClassificationModelInfo extends TransformerInfo {

  val id = "60290377-6A93-495B-BC3E-14F7159B7791"

  val model = new VanillaDecisionTreeClassificationModelInfo{}
  override val parameterGroups = model.parameterGroups

  override def paramMap: ParameterMap = model.paramMap
  override def defaultParamMap: ParameterMap = model.defaultParamMap
}

trait VanillaDecisionTreeClassificationModelInfo extends SparkModelWrapperInfo with ProbabilisticClassifierParameters {
  val id = "90FACCEF-6B9C-46E2-9594-D8A3C7A040F5"
  override val parameterGroups = List(ParameterGroup("", featuresColumnParameter, probabilityColumnParameter, rawPredictionColumnParameter, predictionColumnParameter))
}