package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, Parameters}

trait VectorIndexerModelInfo extends TransformerInfo with Parameters {

  val id = "39406678-2B29-4397-8842-F469E9086DF2"

  val specificParameters = Array.empty[Parameter[_]]
}

object VectorIndexerModelInfo extends VectorIndexerModelInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}