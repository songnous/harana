package com.harana.sdk.shared.models.flow.parameters.library

import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterType}

case class SaveToLibraryParameter(name: String) extends Parameter[String] {

  override def replicate(name: String): SaveToLibraryParameter = copy(name = name)

  val parameterType: ParameterType = ParameterType.SaveToLibrary

}