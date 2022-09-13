package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.selections.MultipleColumnSelection

case class ColumnSelectorParameter(name: String,
                                   description: Option[String],
                                   portIndex: Int) extends AbstractColumnSelectorParameter[MultipleColumnSelection] {

  val parameterType = ParameterType.ColumnSelector

  val isSingle = false

  override def replicate(name: String) = copy(name = name)

}