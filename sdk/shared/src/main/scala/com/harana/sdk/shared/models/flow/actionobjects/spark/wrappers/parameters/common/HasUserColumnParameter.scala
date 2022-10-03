package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection
import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnSelectorParameter}

import scala.language.reflectiveCalls

trait HasUserColumnParameter extends Parameters {

  val userColumnParameter = SingleColumnSelectorParameter("user-column", default = Some(NameSingleColumnSelection("user")), portIndex = 0)
  def getUserColumn = $(userColumnParameter)

}