package com.harana.sdk.shared.models.flow.utils

import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import izumi.reflect.Tag
import com.harana.sdk.utils.ReflectUtils

object TypeUtils {

  // FIXME: Needs to be better way to do this
  def actionObject[T <: ActionObjectInfo](typeTag: Tag[T]): T = {
    val name = "com.harana.sdk.shared.models.flow.catalog." + typeTag.closestClass.getSimpleName
    ReflectUtils.classForName[T](name.substring(0, name.length-4))
  }

  def actionType[T <: ActionTypeInfo](typeTag: Tag[T]): T = {
    val name = "com.harana.sdk.shared.models.flow.catalog." + typeTag.closestClass.getSimpleName
    ReflectUtils.classForName[T](name.substring(0, name.length - 4))
  }

}