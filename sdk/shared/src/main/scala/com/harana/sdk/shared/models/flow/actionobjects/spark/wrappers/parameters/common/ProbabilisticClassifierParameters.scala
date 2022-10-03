package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.SingleColumnCreatorParameter

import scala.language.reflectiveCalls

trait ProbabilisticClassifierParameters extends ClassifierParameters {

  val probabilityColumnParameter = SingleColumnCreatorParameter("probability-column", default = Some("probability"))
  def getProbabilityColumn = $(probabilityColumnParameter)

}