package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.exceptions.FlowError

import java.util.Objects

abstract class Parameter[T] extends java.io.Serializable {

  val name: String

  def constraints = ""

  val parameterType: ParameterType

  def validate(value: T): Vector[FlowError] = Vector.empty

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