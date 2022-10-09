package com.harana.sdk.utils

import scala.scalajs.reflect.Reflect

object ReflectUtils {

  def classForName[T](name: String): T = {
    Reflect.lookupInstantiatableClass(name).get.newInstance().asInstanceOf[T]
  }
}
