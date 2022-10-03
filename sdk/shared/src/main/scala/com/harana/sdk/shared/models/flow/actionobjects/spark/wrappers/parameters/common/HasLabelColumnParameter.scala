package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.selections.{NameSingleColumnSelection, SingleColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnSelectorParameter}

import scala.language.reflectiveCalls

trait HasLabelColumnParameter extends Parameters {

  val labelColumnParameter = SingleColumnSelectorParameter("label-column", default = Some(NameSingleColumnSelection("label")), portIndex = 0)
  def getLabelColumn = $(labelColumnParameter)
  def setLabelColumn(value: SingleColumnSelection): this.type = set(labelColumnParameter, value)

}