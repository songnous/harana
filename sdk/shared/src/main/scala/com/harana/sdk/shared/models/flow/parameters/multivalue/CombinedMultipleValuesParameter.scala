package com.harana.sdk.shared.models.flow.parameters.multivalue

case class CombinedMultipleValuesParameter[T](gridValues: Seq[MultipleValuesParameter[T]]) extends MultipleValuesParameter[T] {
  val values: Seq[T] = gridValues.flatMap(_.values).distinct
}