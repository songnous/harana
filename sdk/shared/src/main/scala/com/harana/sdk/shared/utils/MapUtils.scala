package com.harana.sdk.shared.utils

import scala.reflect.runtime.universe._

object MapUtils {

  def from[T: TypeTag](args: Map[String, Any]): T = {
    val rMirror = runtimeMirror(getClass.getClassLoader)
    val cMirror = rMirror.reflectClass(typeOf[T].typeSymbol.asClass)
    val ctor = typeOf[T].decl(termNames.CONSTRUCTOR).asTerm.alternatives.head.asMethod
    val argList = ctor.paramLists.flatten.map(param => args(param.name.toString))
    cMirror.reflectConstructor(ctor)(argList: _*).asInstanceOf[T]
  }

  def from(cc: Product): Map[String, Any] =
    (Map[String, Any]() /: cc.getClass.getDeclaredFields) {(a, f) =>
      f.setAccessible(true)
      a + (f.getName -> f.get(cc))
    }

  def optionFields[T <: Product:Manifest] = {
    implicitly[Manifest[T]].runtimeClass.getDeclaredFields.filter(_.getType.getSimpleName.equals("Option")).map(_.getName).toList
  }
}