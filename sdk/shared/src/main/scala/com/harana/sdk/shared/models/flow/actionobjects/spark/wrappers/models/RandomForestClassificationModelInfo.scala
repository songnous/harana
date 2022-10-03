package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.ProbabilisticClassifierParameters
import com.harana.sdk.shared.models.flow.actionobjects.{SparkModelWrapperInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, ParameterMap}

trait RandomForestClassificationModelInfo extends TransformerInfo {
 val id = "5E45320A-D21C-4AC0-925C-82AED31B83D3"

  val model = new VanillaRandomForestClassificationModelInfo {}

  override val parameterGroups = model.parameterGroups

  override def paramMap: ParameterMap = model.paramMap

  override def defaultParamMap: ParameterMap = model.defaultParamMap
}

trait VanillaRandomForestClassificationModelInfo extends SparkModelWrapperInfo with ProbabilisticClassifierParameters {

  val id = "94CD2E91-C1E4-49D1-A1FD-6BF110628D1E"

  override val parameterGroups = List.empty[ParameterGroup]

  val specificParameters = Array(
    featuresColumnParameter,
    predictionColumnParameter,
    probabilityColumnParameter,
    rawPredictionColumnParameter
  )
}

object VanillaRandomForestClassificationModelInfo extends VanillaRandomForestClassificationModelInfo