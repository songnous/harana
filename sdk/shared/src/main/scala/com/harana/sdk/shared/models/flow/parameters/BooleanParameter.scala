package com.harana.sdk.shared.models.flow.parameters

case class BooleanParameter(name: String,
                            description: Option[String]) extends Parameter[Boolean] {

  val parameterType = ParameterType.Boolean

  override def replicate(name: String) = copy(name = name)

}
