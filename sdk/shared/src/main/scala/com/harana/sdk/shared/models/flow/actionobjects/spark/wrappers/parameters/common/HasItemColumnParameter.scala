package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection
import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnSelectorParameter}

import scala.language.reflectiveCalls

trait HasItemColumnParameter extends Parameters {

  val itemColumnParameter = SingleColumnSelectorParameter("item-column", default = Some(NameSingleColumnSelection("item")), portIndex = 0)
  def getItemColumn = $(itemColumnParameter)

}