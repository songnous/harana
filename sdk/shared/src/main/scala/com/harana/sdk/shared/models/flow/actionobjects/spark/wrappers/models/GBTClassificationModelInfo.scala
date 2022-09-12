package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.SparkModelWrapperInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters
import com.harana.sdk.shared.models.flow.actionobjects.stringindexingwrapper.StringIndexingWrapperModelInfo

class GBTClassificationModelInfo(model: VanillaGBTClassificationModelInfo) extends StringIndexingWrapperModelInfo(model) {
  val id = "423F9823-706B-4CEA-BD6E-45F749DD5570"
  def this() = this(new VanillaGBTClassificationModelInfo {})
}

trait VanillaGBTClassificationModelInfo extends SparkModelWrapperInfo with PredictorParameters {
  val id = "7C3C18A8-65D5-4D6C-98CA-EF71D510CAFA"
  val parameters = Array(featuresColumnParameter, predictionColumnParameter)
}