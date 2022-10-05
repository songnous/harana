package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.selections.MultipleColumnSelection
import io.circe.generic.JsonCodec

case class ColumnSelectorParameter(name: String,
                                   required: Boolean = false,
                                   default: Option[MultipleColumnSelection] = None,
                                   portIndex: Int) extends AbstractColumnSelectorParameter[MultipleColumnSelection] {

  val parameterType = ParameterType.ColumnSelector

  val isSingle = false

  override def replicate(name: String) = copy(name = name)

}