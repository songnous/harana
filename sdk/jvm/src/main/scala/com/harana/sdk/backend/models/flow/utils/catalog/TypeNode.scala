package com.harana.sdk.backend.models.flow.utils.catalog

import scala.collection.mutable
import scala.reflect.runtime.{universe => ru}

abstract class TypeNode[O] {

  val javaType: Class[_]

  val isTrait: Boolean = javaType.isInterface

  var parent: Option[TypeNode[O]] = None

  val supertraits: mutable.Map[String, TypeNode[O]] = mutable.Map()
  val subclasses: mutable.Map[String, TypeNode[O]] = mutable.Map()
  val subtraits: mutable.Map[String, TypeNode[O]] = mutable.Map()

  val fullName = javaType.getName.replace("$", ".")

  def setParent(node: TypeNode[O]) = parent = Some(node)
  def addSupertrait(node: TypeNode[O]) = supertraits(node.fullName) = node
  def addSuccessor(node: TypeNode[O]) = if (node.isTrait) addSubtrait(node) else addSubclass(node)

  private def addSubclass(node: TypeNode[O]) = subclasses(node.fullName) = node
  private def addSubtrait(node: TypeNode[O]) = subtraits(node.fullName) = node

  def getParentJavaType(upperBoundType: ru.Type): Option[Class[_]]
  def descriptor: TypeDescriptor

  def subclassesInstances: Set[ConcreteClassNode[O]] = {
    val descendants = subclasses.values.map(_.subclassesInstances) ++ subtraits.values.map(_.subclassesInstances)
    TypeNode.sumSets[ConcreteClassNode[O]](descendants)
  }
}

object TypeNode {

  def apply[O](javaType: Class[_]) =
    if (javaType.isInterface)
      TraitNode[O](javaType)
    else
      ClassNode[O](javaType)

  private[TypeNode] def sumSets[T](sets: Iterable[Set[T]]): Set[T] = sets.foldLeft(Set[T]())((x, y) => x ++ y)

}
