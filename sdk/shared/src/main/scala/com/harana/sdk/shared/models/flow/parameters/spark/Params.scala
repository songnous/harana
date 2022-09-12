package com.harana.sdk.shared.models.flow.parameters.spark

import java.lang.reflect.Modifier

trait Params extends Identifiable with Serializable {

  lazy val params: Array[Param[_]] = {
    val methods = this.getClass.getMethods
    methods.filter { m =>
      Modifier.isPublic(m.getModifiers) &&
        classOf[Param[_]].isAssignableFrom(m.getReturnType) &&
        m.getParameterTypes.isEmpty
    }.sortBy(_.getName)
      .map(m => m.invoke(this).asInstanceOf[Param[_]])
  }

  def explainParam(param: Param[_]): String = {
    shouldOwn(param)
    val valueStr = if (isDefined(param)) {
      val defaultValueStr = getDefault(param).map("default: " + _)
      val currentValueStr = get(param).map("current: " + _)
      (defaultValueStr ++ currentValueStr).mkString("(", ", ", ")")
    } else {
      "(undefined)"
    }
    s"${param.name}: ${param.doc} $valueStr"
  }

  def explainParams(): String = {
    params.map(explainParam).mkString("\n")
  }

  final def isSet(param: Param[_]): Boolean = {
    shouldOwn(param)
    paramMap.contains(param)
  }

  final def isDefined(param: Param[_]): Boolean = {
    shouldOwn(param)
    defaultParamMap.contains(param) || paramMap.contains(param)
  }

  def hasParam(paramName: String): Boolean = {
    params.exists(_.name == paramName)
  }

  def getParam(paramName: String): Param[Any] = {
    params.find(_.name == paramName).getOrElse {
      throw new NoSuchElementException(s"Param $paramName does not exist.")
    }.asInstanceOf[Param[Any]]
  }

  final def set[T](param: Param[T], value: T): this.type = {
    set(param -> value)
  }

  protected final def set(param: String, value: Any): this.type = {
    set(getParam(param), value)
  }

  protected final def set(paramPair: ParamPair[_]): this.type = {
    shouldOwn(paramPair.param)
    paramMap.put(paramPair)
    this
  }

  final def get[T](param: Param[T]): Option[T] = {
    shouldOwn(param)
    paramMap.get(param)
  }

  final def clear(param: Param[_]): this.type = {
    shouldOwn(param)
    paramMap.remove(param)
    this
  }

  final def getOrDefault[T](param: Param[T]): T = {
    shouldOwn(param)
    get(param).orElse(getDefault(param)).getOrElse(
      throw new NoSuchElementException(s"Failed to find a default value for ${param.name}"))
  }

  protected final def $[T](param: Param[T]): T = getOrDefault(param)

  protected final def setDefault[T](param: Param[T], value: T): this.type = {
    defaultParamMap.put(param -> value)
    this
  }

  protected final def setDefault(paramPairs: ParamPair[_]*): this.type = {
    paramPairs.foreach { p =>
      setDefault(p.param.asInstanceOf[Param[Any]], p.value)
    }
    this
  }

  final def getDefault[T](param: Param[T]): Option[T] = {
    shouldOwn(param)
    defaultParamMap.get(param)
  }

  final def hasDefault[T](param: Param[T]): Boolean = {
    shouldOwn(param)
    defaultParamMap.contains(param)
  }

  def copy(extra: ParamMap): Params

  protected final def defaultCopy[T <: Params](extra: ParamMap): T = {
    val that = this.getClass.getConstructor(classOf[String]).newInstance(uid)
    copyValues(that, extra).asInstanceOf[T]
  }

  final def extractParamMap(extra: ParamMap): ParamMap = {
    defaultParamMap ++ paramMap ++ extra
  }

  final def extractParamMap(): ParamMap = {
    extractParamMap(ParamMap.empty)
  }

  private val paramMap: ParamMap = ParamMap.empty

  private val defaultParamMap: ParamMap = ParamMap.empty

  private def shouldOwn(param: Param[_]): Unit = {
    require(param.parent == uid && hasParam(param.name), s"Param $param does not belong to $this.")
  }

  protected def copyValues[T <: Params](to: T, extra: ParamMap = ParamMap.empty): T = {
    val map = paramMap ++ extra
    params.foreach { param =>
      // copy default Params
      if (defaultParamMap.contains(param) && to.hasParam(param.name)) {
        to.defaultParamMap.put(to.getParam(param.name), defaultParamMap(param))
      }
      // copy explicitly set Params
      if (map.contains(param) && to.hasParam(param.name)) {
        to.set(param.name, map(param))
      }
    }
    to
  }
}