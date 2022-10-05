package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.selections.SingleColumnSelection
import io.circe.generic.JsonCodec

@JsonCodec
case class SingleColumnSelectorParameter(name: String,
                                         required: Boolean = false,
                                         default: Option[SingleColumnSelection] = None,
                                         portIndex: Int) extends AbstractColumnSelectorParameter[SingleColumnSelection] {

  val parameterType = ParameterType.ColumnSelector

  val isSingle = true

  override def replicate(name: String) = copy(name = name)

}