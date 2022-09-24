package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.exceptions.FlowError

import java.util.Objects

trait Parameter[T] {

  val name: String

  val required: Boolean

  val default: Option[T]

  def constraints = ""

  val parameterType: ParameterType

  def validate(value: T): List[FlowError] = List.empty

  val isGriddable: Boolean = false

  def replicate(name: String): Parameter[T]

  def ->(value: T): ParameterPair[T] = ParameterPair(this, value)

  override def toString = s"Parameter($parameterType -> $name)"

  @unchecked
  override def equals(other: Any): Boolean = other match {
    case that: Parameter[T] =>
      this.isInstanceOf[Parameter[T]] && name == that.name && parameterType == that.parameterType
    case _ => false
  }

  override def hashCode() = Objects.hash(name, parameterType)
}