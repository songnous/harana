package com.harana.sdk.backend.models.flow.utils.catalog

import com.harana.sdk.backend.models.flow.utils.TypeUtils
import scala.reflect.runtime.{universe => ru}

class TraitNode[O](override val javaType: Class[_]) extends TypeNode[O] {

  override def getParentJavaType(upperBoundType: ru.Type): Option[Class[_]] = {
    val t = TypeUtils.classToType(javaType)
    val baseTypes = t.baseClasses.map(TypeUtils.symbolToType)
    val mirror = TypeUtils.classMirror(javaType)
    val baseJavaTypes = baseTypes.filter(_ <:< upperBoundType).map(TypeUtils.typeToClass(_, mirror))
    baseJavaTypes.find(!_.isInterface)
  }

  override def descriptor: TypeDescriptor =
    TraitDescriptor(fullName, (supertraits.values ++ parent).map(_.fullName).toList)

}

object TraitNode {
  def apply[O](javaType: Class[_]): TraitNode[O] = new TraitNode[O](javaType)
}
