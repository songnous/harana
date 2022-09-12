package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnSelectorParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

import scala.language.reflectiveCalls

trait HasItemColumnParameter extends Parameters {

  val itemColumnParameter = SingleColumnSelectorParameter("item column", Some("The column for item ids."),
    portIndex = 0
  )

  def getItemColumn = $(itemColumnParameter)
  setDefault(itemColumnParameter, NameSingleColumnSelection("item"))
}