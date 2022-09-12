

package com.harana.executor.spark.metrics.common

import org.json4s.StringInput

object Json4sWrapper {

  private val parseMethod = {
    try {
      org.json4s.jackson.JsonMethods.getClass.getDeclaredMethod("parse", classOf[org.json4s.JsonInput], classOf[Boolean])
    }catch {
      case ne: NoSuchMethodException =>
        org.json4s.jackson.JsonMethods.getClass.getDeclaredMethod("parse", classOf[org.json4s.JsonInput], classOf[Boolean], classOf[Boolean])
    }
  }

  private val methodsObject = {
    val objectName = "org.json4s.jackson.JsonMethods$"
    val cons = Class.forName(objectName).getDeclaredConstructors();
    cons(0).setAccessible(true);
    val jsonMethodObject:org.json4s.jackson.JsonMethods = cons(0).newInstance().asInstanceOf[org.json4s.jackson.JsonMethods]
    jsonMethodObject
  }

  def parse(json:String):org.json4s.JsonAST.JValue = {
    try {
      if (parseMethod.getParameterCount == 2) {
        parseMethod.invoke(methodsObject, new StringInput(json), boolean2Boolean(false))
          .asInstanceOf[org.json4s.JsonAST.JValue]
      } else {
        parseMethod.invoke(methodsObject, new StringInput(json), boolean2Boolean(false), boolean2Boolean(true))
          .asInstanceOf[org.json4s.JsonAST.JValue]
      }
    }catch {
      case t:Throwable => {
        t.printStackTrace()
        throw t
      }
    }
  }
}
