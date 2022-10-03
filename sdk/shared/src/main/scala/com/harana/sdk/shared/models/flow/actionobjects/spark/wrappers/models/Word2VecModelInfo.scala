package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.Word2VecParameters
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup}

trait Word2VecModelInfo extends TransformerInfo with Word2VecParameters {

  val id = "F0AC9EF8-D86B-4283-94C1-705627C75F73"

  val specificParameters = Array[Parameter[_]](
    maxIterationsParameter,
    stepSizeParameter,
    seedParameter,
    vectorSizeParameter,
    numPartitionsParameter,
    minCountParameter)
}

object Word2VecModelInfo extends Word2VecModelInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}