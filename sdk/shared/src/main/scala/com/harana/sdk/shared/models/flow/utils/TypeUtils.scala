package com.harana.sdk.shared.models.flow.utils

import com.harana.sdk.shared.models.flow.parameters.exceptions.NoArgumentConstructorRequiredError
import java.lang.reflect.Constructor
import scala.reflect.runtime.universe.Type
import scala.reflect.runtime.{universe => ru}

/** Holds methods used for manipulating objects representing types. */
object TypeUtils {

  def classMirror(c: Class[_]): ru.Mirror = ru.runtimeMirror(c.getClassLoader)

  def classToType[T](c: Class[T]): ru.Type = classMirror(c).classSymbol(c).toType

  def typeToClass(t: ru.Type, mirror: ru.Mirror): Class[_] = mirror.runtimeClass(t.typeSymbol.asClass)

  def typeTagToClass[T](t: ru.TypeTag[T]): Class[T] =
    t.mirror.runtimeClass(t.tpe.typeSymbol.asClass).asInstanceOf[Class[T]]

  def symbolToType(s: ru.Symbol): ru.Type = s.asClass.toType

  def isParametrized(t: ru.Type): Boolean = t.typeSymbol.asClass.typeParams.nonEmpty

  def isAbstract(c: Class[_]): Boolean =
    classToType(c).typeSymbol.asClass.isAbstract

  def constructorForClass[T](c: Class[T]): Option[Constructor[T]] = {
    val constructors = c.getConstructors
    val isParameterLess: (Constructor[_] => Boolean) = constructor => constructor.getParameterTypes.isEmpty
    constructors.find(isParameterLess).map(_.asInstanceOf[Constructor[T]])
  }

  def constructorForTypeTag[T](t: ru.TypeTag[T]): Option[Constructor[T]] =
    constructorForClass(typeTagToClass(t))

  def constructorForType(t: ru.Type, mirror: ru.Mirror): Option[Constructor[_]] =
    constructorForClass(typeToClass(t, mirror))

  def createInstance[T](constructor: Constructor[T]): T =
    constructor.newInstance()

  def instanceOfType[T](typeTag: ru.TypeTag[T]): T =
    createInstance(constructorForTypeTag(typeTag).getOrElse {
      throw NoArgumentConstructorRequiredError(typeTag.tpe.typeSymbol.asClass.name.decodedName.toString).toException
    })

  private val TypeSeparator = " with "

  private def cutAfter(ch: Char)(s: String) = {
    val found = s.lastIndexOf(ch)
    if (found == -1) s else s.substring(0, found)
  }

  def describeType(t: Type): Seq[String] =
    t.toString.split(TypeSeparator).map(cutAfter('[')).toIndexedSeq

  /** Helper method that converts scala types to readable strings. */
  def typeToString(t: Type) =
    describeType(t).map(_.split("\\.").toList.last).mkString(TypeSeparator)

}
