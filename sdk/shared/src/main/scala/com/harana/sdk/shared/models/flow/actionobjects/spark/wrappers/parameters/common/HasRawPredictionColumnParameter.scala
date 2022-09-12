package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnSelectorParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

import scala.language.reflectiveCalls

trait HasRawPredictionColumnParameter extends Parameters {

  val rawPredictionColumnParameter = SingleColumnSelectorParameter("raw prediction column", Some("The raw prediction (confidence) column."),
    portIndex = 0
  )

  setDefault(rawPredictionColumnParameter, NameSingleColumnSelection("rawPrediction"))
}