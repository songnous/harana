package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection
import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnSelectorParameter}

import scala.language.reflectiveCalls

trait HasInputColumnParameter extends Parameters {

  val inputColumnParameter = SingleColumnSelectorParameter("input-column", portIndex = 0)
  def setInputColumn(value: String): this.type = set(inputColumnParameter, NameSingleColumnSelection(value))

}