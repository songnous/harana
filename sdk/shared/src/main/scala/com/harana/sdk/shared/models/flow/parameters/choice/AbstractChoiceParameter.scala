package com.harana.sdk.shared.models.flow.parameters.choice

import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.parameters.exceptions.NoArgumentConstructorRequiredError
import izumi.reflect.Tag

import scala.reflect.runtime.universe._

abstract class AbstractChoiceParameter[T <: Choice, U](implicit tag: Tag[T]) extends Parameter[U] {

  val choiceInstances = {
    List()
// FIXME
//    val directSubclasses = tag.tpe.typeSymbol.asClass.knownDirectSubclasses
//    val instances =
//      for (symbol <- directSubclasses)
//        yield TypeUtils
//          .constructorForType(symbol.typeSignature, tag.mirror)
//          .getOrElse(throw NoArgumentConstructorRequiredError(symbol.asClass.name.decodedName.toString).toException)
//          .newInstance()
//          .asInstanceOf[T]
//    val allSubclassesDeclared = instances.forall(instance => instance.choiceOrder.contains(instance.getClass))
//    require(allSubclassesDeclared,
//      "Not all choices were declared in choiceOrder map. " +
//        s"Declared: {${instances.head.choiceOrder.map(smartClassName(_)).mkString(", ")}}, " +
//        s"All choices: {${instances.map(i => smartClassName(i.getClass)).mkString(", ")}}"
//    )
//    instances.toList.sortBy(choice => choice.choiceOrder.indexOf(choice.getClass))
  }

  private def smartClassName[T](clazz: Class[T]) = {
    val simpleName = clazz.getSimpleName
    if (simpleName == null) clazz.getName else simpleName
  }

//  val choiceInstancesByName = choiceInstances.map(c => c.name -> c).toMap

  val choiceInstancesByName = Map()
}
