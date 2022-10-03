package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, Parameters}

import scala.language.reflectiveCalls

trait PCAModelInfo extends TransformerInfo with Parameters {

  val id = "1DF5AA27-4001-4669-902F-3D543CC0E313"

  val specificParameters = Array.empty[Parameter[_]]

}

object PCAModelInfo extends PCAModelInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}