package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnSelectorParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

import scala.language.reflectiveCalls

trait HasUserColumnParameter extends Parameters {

  val userColumnParameter = SingleColumnSelectorParameter("user column", Some("The column for user ids."),
    portIndex = 0
  )

  def getUserColumn = $(userColumnParameter)
  setDefault(userColumnParameter, NameSingleColumnSelection("user"))
}