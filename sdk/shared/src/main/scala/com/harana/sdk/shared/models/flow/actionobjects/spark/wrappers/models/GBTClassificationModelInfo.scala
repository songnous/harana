package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters
import com.harana.sdk.shared.models.flow.actionobjects.{SparkModelWrapperInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.parameters.ParameterMap

trait GBTClassificationModelInfo extends TransformerInfo {
  val id = "423F9823-706B-4CEA-BD6E-45F749DD5570"

  val model = new VanillaDecisionTreeClassificationModelInfo {}
  val parameters = model.parameters

  override def paramMap: ParameterMap = model.paramMap

  override def defaultParamMap: ParameterMap = model.defaultParamMap
}

trait VanillaGBTClassificationModelInfo extends SparkModelWrapperInfo with PredictorParameters {
  val id = "7C3C18A8-65D5-4D6C-98CA-EF71D510CAFA"
  val parameters = Array(featuresColumnParameter, predictionColumnParameter)
}