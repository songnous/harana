package com.harana.sdk.shared.models.flow.parameters

case class BooleanParameter(name: String,
                            required: Boolean = false,
                            default: Option[Boolean] = None) extends Parameter[Boolean] {

  val parameterType = ParameterType.Boolean

  override def replicate(name: String) = copy(name = name)

}
