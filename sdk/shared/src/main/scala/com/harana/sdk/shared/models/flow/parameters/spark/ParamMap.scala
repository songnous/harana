package com.harana.sdk.shared.models.flow.parameters.spark

import scala.collection.mutable

final class ParamMap private (private val map: mutable.Map[Param[Any], Any]) extends Serializable {

  def this() = this(mutable.Map.empty)

  def put[T](param: Param[T], value: T): this.type = put(param -> value)

  def put(paramPairs: ParamPair[_]*): this.type = {
    paramPairs.foreach { p =>
      map(p.param.asInstanceOf[Param[Any]]) = p.value
    }
    this
  }

  def get[T](param: Param[T]): Option[T] = {
    map.get(param.asInstanceOf[Param[Any]]).asInstanceOf[Option[T]]
  }

  def getOrElse[T](param: Param[T], default: T): T = {
    get(param).getOrElse(default)
  }

  def apply[T](param: Param[T]): T = {
    get(param).getOrElse {
      throw new NoSuchElementException(s"Cannot find param ${param.name}.")
    }
  }

  def contains(param: Param[_]): Boolean = {
    map.contains(param.asInstanceOf[Param[Any]])
  }

  def remove[T](param: Param[T]): Option[T] = {
    map.remove(param.asInstanceOf[Param[Any]]).asInstanceOf[Option[T]]
  }

  def filter(parent: Params): ParamMap = {
    // Don't use filterKeys because mutable.Map#filterKeys
    // returns the instance of collections.Map, not mutable.Map.
    // Otherwise, we get ClassCastException.
    // Not using filterKeys also avoid SI-6654
    val filtered = map.filter { case (k, _) => k.parent == parent.uid }
    new ParamMap(filtered)
  }

  def copy: ParamMap = new ParamMap(map.clone())

  override def toString: String = {
    map.toSeq.sortBy(_._1.name).map { case (param, value) =>
      s"\t${param.parent}-${param.name}: $value"
    }.mkString("{\n", ",\n", "\n}")
  }

  def ++(other: ParamMap): ParamMap = {
    // TODO: Provide a better method name for Java users.
    new ParamMap(this.map ++ other.map)
  }

  def ++=(other: ParamMap): this.type = {
    // TODO: Provide a better method name for Java users.
    this.map ++= other.map
    this
  }

  def toSeq: Seq[ParamPair[_]] = {
    map.toSeq.map { case (param, value) =>
      ParamPair(param, value)
    }
  }

  def size: Int = map.size
}

object ParamMap {
  def empty: ParamMap = new ParamMap()

  def apply(paramPairs: ParamPair[_]*): ParamMap = {
    new ParamMap().put(paramPairs: _*)
  }
}
