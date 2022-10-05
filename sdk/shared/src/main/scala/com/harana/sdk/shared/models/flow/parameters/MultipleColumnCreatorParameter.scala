package com.harana.sdk.shared.models.flow.parameters

import io.circe.generic.JsonCodec

@JsonCodec
case class MultipleColumnCreatorParameter(name: String,
                                          required: Boolean = false,
                                          default: Option[Array[String]] = None) extends Parameter[Array[String]] {

  val parameterType = ParameterType.MultipleColumnCreator

  override def replicate(name: String): MultipleColumnCreatorParameter = copy(name = name)

}
