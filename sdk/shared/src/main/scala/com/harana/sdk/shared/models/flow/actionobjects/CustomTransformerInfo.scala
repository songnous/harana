package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterPair}
import com.harana.sdk.shared.models.flow.parameters.custom.InnerWorkflow

case class CustomTransformerInfo(publicParametersWithValues: Seq[ParameterWithValues[_]] = Seq.empty) extends TransformerInfo {

  val id = "A7D6EF00-5B5F-44AA-8F49-ECA974703E8E"

  val parameters = Left(publicParametersWithValues.map(_.param).toList)

  publicParametersWithValues.foreach {
    case ParameterWithValues(param, defaultValue, setValue) =>
      val paramAny = param.asInstanceOf[Parameter[Any]]
      defaultValue.foreach(defaultValue => defaultParamMap.put(ParameterPair(paramAny, defaultValue)))
      setValue.foreach(setValue => paramMap.put(ParameterPair(paramAny, setValue)))
  }

  def getParameter(parameters: List[Parameter[_]], name: String) =
    parameters.find(_.name == name).get
}

case class ParameterWithValues[T](param: Parameter[_], defaultValue: Option[T] = None, setValue: Option[T] = None)