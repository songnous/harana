package com.harana.sdk.backend.models.flow.utils.catalog

import com.harana.sdk.shared.models.designer.flow.utils.catalog.exceptions.NoParameterlessConstructorInClassError
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import java.lang.reflect.Constructor

class ConcreteClassNode[O](javaType: Class[_]) extends ClassNode[O](javaType) {

  val constructor: Constructor[_] = TypeUtils.constructorForClass(javaType) match {
    case Some(parameterLessConstructor) => parameterLessConstructor
    case None => throw NoParameterlessConstructorInClassError(this.javaTypeName).toException
  }

  def createInstance[T <: O]: T =
    TypeUtils.createInstance[T](constructor.asInstanceOf[Constructor[T]])

  override def subclassesInstances: Set[ConcreteClassNode[O]] =
    super.subclassesInstances + this

}