package com.harana.sdk.shared.models.flow.parameters

case class MultipleColumnCreatorParameter(name: String) extends Parameter[Array[String]] {

  val parameterType = ParameterType.MultipleColumnCreator

  override def replicate(name: String): MultipleColumnCreatorParameter = copy(name = name)

}
