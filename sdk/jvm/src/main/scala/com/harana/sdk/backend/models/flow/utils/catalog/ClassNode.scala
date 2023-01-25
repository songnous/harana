package com.harana.sdk.backend.models.flow.utils.catalog

import com.harana.sdk.backend.models.flow.utils.TypeUtils
import scala.language.existentials
import scala.reflect.runtime.{universe => ru}

class ClassNode[O](override val javaType: Class[_]) extends TypeNode[O] {

  def javaTypeName = javaType.getCanonicalName

  override def getParentJavaType(upperBoundType: ru.Type) = {
    val parentJavaType = javaType.getSuperclass
    val parentType = TypeUtils.classToType(parentJavaType)
    if (parentType <:< upperBoundType) Some(parentJavaType) else None
  }

  override def descriptor = {
    val parentName = if (parent.nonEmpty) Some(parent.get.fullName) else None
    ClassDescriptor(fullName, parentName, supertraits.values.map(_.fullName).toList)
  }
}

object ClassNode {
  def apply[O](javaType: Class[_]) =
    if (TypeUtils.isAbstract(javaType))
      new ClassNode[O](javaType)
    else
      new ConcreteClassNode[O](javaType)
}