package com.harana.sdk.shared.models.flow.parameters

import ParameterType._
import com.harana.sdk.shared.models.flow.exceptions.FlowError

import java.util.Objects

abstract class Parameter[T] extends java.io.Serializable {

  val name: String
  val description: Option[String]

  def constraints = ""

  val parameterType: ParameterType

  def validate(value: T): Vector[FlowError] = Vector.empty

  val isGriddable: Boolean = false

  def replicate(name: String): Parameter[T]

  def ->(value: T): ParameterPair[T] = ParameterPair(this, value)

  override def toString = s"Parameter($parameterType -> $name)"

  override def equals(other: Any): Boolean = other match {
    case that: Parameter[T] => this.isInstanceOf[Parameter[T]] && name == that.name && description == that.description && parameterType == that.parameterType
    case _ => false
  }

  override def hashCode() = Objects.hash(name, description, parameterType)
}