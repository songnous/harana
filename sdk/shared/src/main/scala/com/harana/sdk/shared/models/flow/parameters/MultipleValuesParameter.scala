package com.harana.sdk.shared.models.flow.parameters

import io.circe.generic.JsonCodec

@JsonCodec
sealed trait MultipleValuesParameter[T] {
  def values: Seq[T]
}

object MultipleValuesParameter {

  @JsonCodec
  case class CombinedMultipleValuesParameter[T](gridValues: Seq[MultipleValuesParameter[T]]) extends MultipleValuesParameter[T] {
    val values: Seq[T] = gridValues.flatMap(_.values).distinct
  }

  @JsonCodec
  case class ValuesSequenceParameter[T](sequence: List[T]) extends MultipleValuesParameter[T] {
    def values: List[T] = sequence
  }
}