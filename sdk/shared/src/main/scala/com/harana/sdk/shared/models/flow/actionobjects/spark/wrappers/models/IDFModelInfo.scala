package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, Parameters}

trait IDFModelInfo extends TransformerInfo with Parameters {

  val id = "72AD4553-6035-459F-AC89-D5BD8B34AA6A"

  val specificParameters = Array.empty[Parameter[_]]
}

object IDFModelInfo extends IDFModelInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}