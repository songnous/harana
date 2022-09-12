package com.harana.sdk.shared.models.flow.parameters

import scala.collection.mutable

case class ParameterMap (private val map: mutable.Map[Parameter[Any], Any]) extends Serializable {

  def put[T](param: Parameter[T], value: T): this.type = put(param -> value)
  def put(paramPairs: ParameterPair[_]*): this.type = {
    paramPairs.foreach(p => map(p.param.asInstanceOf[Parameter[Any]]) = p.value)
    this
  }

  def get[T](param: Parameter[T]) = map.get(param.asInstanceOf[Parameter[Any]]).asInstanceOf[Option[T]]
  def getOrElse[T](param: Parameter[T], default: T) = get(param).getOrElse(default)
  def apply[T](param: Parameter[T]) = get(param).getOrElse(throw new NoSuchElementException(s"Cannot find param ${param.name}."))
  def contains(param: Parameter[_]) = map.contains(param.asInstanceOf[Parameter[Any]])
  def remove[T](param: Parameter[T]) = map.remove(param.asInstanceOf[Parameter[Any]]).asInstanceOf[Option[T]]
  def ++(other: ParameterMap) = ParameterMap(this.map ++ other.map)

  def ++=(other: ParameterMap) = {
    this.map ++= other.map
    this
  }

  def toSeq = map.toSeq.map { case (param, value) => ParameterPair(param, value) }
  def size: Int = map.size

}

object ParameterMap {
  def apply(): ParameterMap = ParameterMap(mutable.Map.empty[Parameter[Any], Any])
  def apply(paramPairs: ParameterPair[_]*): ParameterMap = ParameterMap().put(paramPairs: _*)

  def empty: ParameterMap = ParameterMap()
}
