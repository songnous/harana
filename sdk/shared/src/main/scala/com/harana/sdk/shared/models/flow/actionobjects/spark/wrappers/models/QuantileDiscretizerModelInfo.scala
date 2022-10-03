package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, Parameters}

import scala.language.reflectiveCalls

trait QuantileDiscretizerModelInfo extends TransformerInfo with Parameters {

  val id = "1FC09D26-384D-4558-B086-D3941F10E729"

  val specificParameters = Array.empty[Parameter[_]]
}

object QuantileDiscretizerModelInfo extends QuantileDiscretizerModelInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}