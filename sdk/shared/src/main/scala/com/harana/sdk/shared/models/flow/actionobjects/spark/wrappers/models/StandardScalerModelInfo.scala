package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, Parameters}

trait StandardScalerModelInfo extends TransformerInfo with Parameters {

  val id = "C6C1BDC1-2E55-45DA-ADCE-F30B1457212E"

  val specificParameters = Array.empty[Parameter[_]]

}

object StandardScalerModelInfo extends StandardScalerModelInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}