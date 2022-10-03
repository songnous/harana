package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.SingleColumnCreatorParameter

import scala.language.reflectiveCalls

trait ClassifierParameters extends PredictorParameters {

  val rawPredictionColumnParameter = SingleColumnCreatorParameter("raw-prediction-column", default = Some("rawPrediction"))
  def getRawPredictionColumn = $(rawPredictionColumnParameter)

}
