package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow.utils.catalog.GenericCatalog
import com.harana.sdk.backend.models.flow.utils.catalog.exceptions.NoParameterlessConstructorInClassError
import com.harana.sdk.shared.models.flow.{ActionTypeInfo, ActionObjectInfo}
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import java.lang.reflect.Constructor
import scala.reflect.ClassTag

object Catalog {

  class ActionCatalog extends GenericCatalog[ActionType]
  class ActionObjectCatalog extends GenericCatalog[ActionObjectInfo]

  def actionForActionInfo[A <: ActionTypeInfo](info: A)(implicit ct: ClassTag[A]): ActionType = {
    val className = info.getClass.getCanonicalName.replace("backend", "shared")
    val cls = Class.forName(className.substring(0, className.length - 4))

    val constructor = TypeUtils.constructorForClass(cls) match {
      case Some(parameterLessConstructor) => parameterLessConstructor
      case None => throw NoParameterlessConstructorInClassError(cls.getSimpleName).toException
    }

    TypeUtils.createInstance[ActionType](constructor.asInstanceOf[Constructor[ActionType]])
  }

}
