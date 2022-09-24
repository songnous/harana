package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnSelectorParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

import scala.language.reflectiveCalls

trait HasPredictionColumnSelectorParameter extends Parameters {

  val predictionColumnParameter = SingleColumnSelectorParameter("prediction column", default = Some(NameSingleColumnSelection("prediction")), portIndex = 0)

}