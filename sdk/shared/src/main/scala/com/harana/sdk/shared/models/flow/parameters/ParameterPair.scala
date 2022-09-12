package com.harana.sdk.shared.models.flow.parameters

case class ParameterPair[T](param: Parameter[T], values: Seq[T]) {
  require(values.nonEmpty)
  values.foreach(param.validate)
  lazy val value = values.head
}

object ParameterPair {
  def apply[T](param: Parameter[T], value: T): ParameterPair[T] = ParameterPair(param, Seq(value))
}
