package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.SingleColumnCreatorParameter
import scala.language.reflectiveCalls

trait ProbabilisticClassifierParameters extends ClassifierParameters {

  val probabilityColumnParameter = SingleColumnCreatorParameter("probability column", Some("The column for predicted class conditional probabilities."))

  def getProbabilityColumn = $(probabilityColumnParameter)
  setDefault(probabilityColumnParameter, "probability")
}