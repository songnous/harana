package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{Parameter, Parameters}

trait StringTokenizerInfo extends TransformerInfo with Parameters {
  val id = "598E9D3C-7E5E-45ED-A250-558F3AC45FB9"

  val parameters = Left(Array.empty[Parameter[_]])
}

object StringTokenizerInfo extends StringTokenizerInfo