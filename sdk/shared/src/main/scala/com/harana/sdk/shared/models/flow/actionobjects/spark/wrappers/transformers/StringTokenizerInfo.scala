package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, Parameters}

trait StringTokenizerInfo extends TransformerInfo with Parameters {
  val id = "598E9D3C-7E5E-45ED-A250-558F3AC45FB9"
}

object StringTokenizerInfo extends StringTokenizerInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}