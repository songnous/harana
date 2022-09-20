package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.selections.{NameSingleColumnSelection, SingleColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnSelectorParameter}

import scala.language.reflectiveCalls

trait HasLabelColumnParameter extends Parameters {

  val labelColumnParameter = SingleColumnSelectorParameter("label column", portIndex = 0)
  setDefault(labelColumnParameter, NameSingleColumnSelection("label"))
  def getLabelColumn = $(labelColumnParameter)
  def setLabelColumn(value: SingleColumnSelection): this.type = set(labelColumnParameter, value)

}