package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.actionobjects.descriptions.HasInferenceResult
import com.harana.sdk.shared.models.flow.exceptions.{FlowError, FlowMultiError}
import com.harana.sdk.shared.models.flow.parameters.exceptions.ParamValueNotProvidedError
import io.circe.Json

trait Parameters extends Serializable with HasInferenceResult {

  val parameterGroups: List[ParameterGroup]

  lazy val allParameters = parameterGroups.flatMap(_.parameters)
  private val _paramMap: ParameterMap = ParameterMap.empty
  private val _defaultParamMap: ParameterMap = ParameterMap.empty

  private lazy val parametersByName = allParameters.map(param => param.name -> param).toMap[String, Parameter[_]]

  def paramMap: ParameterMap = _paramMap

  def defaultParamMap: ParameterMap = _defaultParamMap

  def customValidateParameters = List.empty[FlowError]

  def validateParameters = {
    val singleParameterErrors = allParameters.flatMap { param =>
      if (isDefined(param))
        param.asInstanceOf[Parameter[Any]].validate($(param))
      else
        List(ParamValueNotProvidedError(param.name))
    }

    val customValidationErrors = customValidateParameters
    singleParameterErrors ++ customValidationErrors
  }

  def validateDynamicParameters(parameters: Parameters*) = {
    val validationResult = parameters.flatMap(param => param.validateParameters).toList
    if (validationResult.nonEmpty) throw FlowMultiError(validationResult).toException
  }

  def isSet(param: Parameter[_]) = paramMap.contains(param)
  def isDefined(param: Parameter[_]) = defaultParamMap.contains(param) || paramMap.contains(param)
  def hasParameter(parameterName: String) = allParameters.exists(_.name == parameterName)
  def getParam(parameterName: String) = allParameters.find(_.name == parameterName).getOrElse {
    throw new NoSuchElementException(s"Param $parameterName does not exist.")
  }.asInstanceOf[Parameter[Any]]

  def set[T](param: Parameter[T], value: T): this.type = set(param -> value)
  def set(param: String, value: Any): this.type = set(getParam(param), value)

  def set(paramPair: ParameterPair[_]): this.type = {
    paramMap.put(paramPair)
    this
  }

  def set(paramPairs: ParameterPair[_]*): this.type = {
    paramMap.put(paramPairs: _*)
    this
  }

  def set(paramMap: ParameterMap): this.type = {
    set(paramMap.toSeq: _*)
    this
  }

  def clear(param: Parameter[_]): this.type = {
    paramMap.remove(param)
    this
  }

  def get[T](param: Parameter[T]): Option[T] = paramMap.get(param)
  def getDefault[T](param: Parameter[T]) = param.default.orElse(defaultParamMap.get(param))
  def getOrDefaultOption[T](param: Parameter[T]) = get(param).orElse(getDefault(param))
  def getOrDefault[T](param: Parameter[T]): T = getOrDefaultOption(param).getOrElse {
    throw ParamValueNotProvidedError(param.name).toException
  }

  def $[T](param: Parameter[T]) = getOrDefault(param)

  def setDefault[T](param: Parameter[T], value: T): this.type = {
    defaultParamMap.put(param -> value)
    this
  }

  def setDefault(paramPairs: ParameterPair[_]*): this.type = {
    paramPairs.foreach(p => setDefault(p.param.asInstanceOf[Parameter[Any]], p.value))
    this
  }

  def hasDefault[T](param: Parameter[T]) = defaultParamMap.contains(param)

  def extractParameterMap(extra: ParameterMap = ParameterMap.empty) = defaultParamMap ++ paramMap ++ extra

  def replicate(extra: ParameterMap = ParameterMap.empty): this.type = {
    val that = this.getClass.getConstructor().newInstance().asInstanceOf[this.type]
    copyValues(that, extra)
  }

  def sameAs(other: Parameters): Boolean =
    other.getClass == this.getClass && other.parameterValuesToJson == this.parameterValuesToJson

  def copyValues[T <: Parameters](to: T, extra: ParameterMap = ParameterMap.empty): T = {
    val map = paramMap ++ extra
    allParameters.foreach { param =>
      if (map.contains(param) && to.hasParameter(param.name)) to.set(param.name, map(param))
    }
    to
  }

  def parameterValuesToJson: Json = {
    Json.Null
//    val fields = for (param <- parameters) yield {
//      get(param).map {
//        case paramValue => param.name -> param.anyValueToJson(paramValue)
//      }
//    }
//    JsObject(fields.flatten.toMap)
  }

  def objectExpectedException(jsValue: Any) = ???

  private def getMultiValueParameter(json: Json, parameter: Parameter[_]): Option[ParameterPair[_]] = {
    parameter match {
      case _: IntParameter |
           _: LongParameter |
           _: FloatParameter |
           _: DoubleParameter |
           _: NumericParameter =>
        createMultiValueParameter[Double](json, parameter)
      case _ => None
    }
  }

  def setParametersFromJson(json: Json, ignoreNulls: Boolean = false): this.type = {
    null
  }

  def parametersToJson: Json =
    null

  def parameterPairsFromJson(json: Json): Seq[ParameterPair[_]] = null

  private def createMultiValueParameter[T](json: Json, parameter: Parameter[_]): Option[ParameterPair[T]] = {
    None
  }
}