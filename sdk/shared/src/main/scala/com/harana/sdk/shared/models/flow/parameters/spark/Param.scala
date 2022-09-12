package com.harana.sdk.shared.models.flow.parameters.spark

class Param[T](val parent: String, val name: String, val doc: String, val isValid: T => Boolean) extends Serializable {

  def this(parent: Identifiable, name: String, doc: String, isValid: T => Boolean) =
    this(parent.uid, name, doc, isValid)

  def w(value: T): ParamPair[T] = this -> value

  def ->(value: T): ParamPair[T] = ParamPair(this, value)
  
  private[this] val stringRepresentation = s"${parent}__$name"

  override final def toString: String = stringRepresentation
  override final def hashCode: Int = toString.##
  override final def equals(obj: Any): Boolean = {
    obj match {
      case p: Param[_] => (p.parent == parent) && (p.name == name)
      case _ => false
    }
  }
}