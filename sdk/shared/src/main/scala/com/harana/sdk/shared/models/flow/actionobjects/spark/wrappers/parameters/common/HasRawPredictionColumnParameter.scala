package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection
import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnSelectorParameter}

import scala.language.reflectiveCalls

trait HasRawPredictionColumnParameter extends Parameters {

  val rawPredictionColumnParameter = SingleColumnSelectorParameter("raw-prediction-column", default = Some(NameSingleColumnSelection("rawPrediction")), portIndex = 0)

}