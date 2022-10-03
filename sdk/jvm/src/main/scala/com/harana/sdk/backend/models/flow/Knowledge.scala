package com.harana.sdk.backend.models.flow

import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import scala.reflect.runtime.{universe => ru}

case class Knowledge[+T <: ActionObjectInfo] private[Knowledge](types: Seq[T]) {

  def filterTypes(t: ru.Type) = Knowledge(types.filter(x => TypeUtils.classToType(x.getClass) <:< t): _*)

  def ++[U >: T <: ActionObjectInfo](other: Knowledge[U]): Knowledge[U] = Knowledge[U](types ++ other.types: _*)

  override def equals(other: Any) = other match {
      case that: Knowledge[_] => types.toSet == that.types.toSet
      case _  => false
    }

  def single: T = {
    require(types.nonEmpty, "Expected at least one inferred type, but got 0")
    types.head
  }

  def size: Int = types.size
  override def hashCode(): Int = types.hashCode()
  override def toString = s"Knowledge($types)"
}

object Knowledge {
  def apply[T <: ActionObjectInfo](args: T*)(implicit s: DummyImplicit): Knowledge[T] = new Knowledge[T](args.distinct)
  def apply[T <: ActionObjectInfo](types: Seq[T]): Knowledge[T] = new Knowledge[T](types.distinct)
  def apply[T <: ActionObjectInfo](types: Set[T]): Knowledge[T] = new Knowledge[T](types.toSeq)
  def apply[T <: ActionObjectInfo](dKnowledges: Iterable[Knowledge[T]]): Knowledge[T] = dKnowledges.foldLeft(new Knowledge[T](Seq.empty))(_ ++ _)
}