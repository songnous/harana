package com.harana.sdk.shared.models.flow.parameters.library

import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterType}

case class LoadFromLibraryParameter(name: String,
                                    required: Boolean = false,
                                    default: Option[String] = None) extends Parameter[String] {

  override def replicate(name: String): LoadFromLibraryParameter = copy(name = name)

  val parameterType: ParameterType = ParameterType.LoadFromLibrary

}