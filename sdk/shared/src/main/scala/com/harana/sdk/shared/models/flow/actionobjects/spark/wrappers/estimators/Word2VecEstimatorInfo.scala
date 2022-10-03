package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.Word2VecParameters
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup}

trait Word2VecEstimatorInfo extends EstimatorInfo with Word2VecParameters {

  val id = "F2ADB686-9EA7-4E72-A0CE-6EE91E0E2B54"

  override val stepSizeDefault = 0.025

  override val maxIterationsDefault = 1

  val specificParameters = Array[Parameter[_]](
    maxIterationsParameter,
    stepSizeParameter,
    seedParameter,
    vectorSizeParameter,
    numPartitionsParameter,
    minCountParameter
  )
}

object Word2VecEstimatorInfo extends Word2VecEstimatorInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}