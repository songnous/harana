package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnCreatorParameter}

import scala.language.reflectiveCalls

trait HasPredictionColumnCreatorParameter extends Parameters {

  val predictionColumnParameter = SingleColumnCreatorParameter("prediction column", Some("The prediction column created during model scoring."))

  def getPredictionColumn = $(predictionColumnParameter)
  setDefault(predictionColumnParameter, "prediction")
}