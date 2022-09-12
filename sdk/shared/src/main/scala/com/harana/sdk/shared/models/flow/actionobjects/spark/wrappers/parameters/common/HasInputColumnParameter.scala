package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnSelectorParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

import scala.language.reflectiveCalls

trait HasInputColumnParameter extends Parameters {

  val inputColumnParameter = SingleColumnSelectorParameter("input column", Some("The input column name."),
    portIndex = 0
  )

  def setInputColumn(value: String): this.type = set(inputColumnParameter, NameSingleColumnSelection(value))
}
