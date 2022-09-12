package com.harana.sdk.shared.models.flow.parameters.multivalue

case class ValuesSequenceParameter[T](sequence: List[T]) extends MultipleValuesParameter[T] {
  def values: List[T] = sequence
}

object ValuesSequenceParameter {
  val paramType = "seq"
}