package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.flow.Action0To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import izumi.reflect.Tag

trait EstimatorAsFactoryInfo[E <: EstimatorInfo] extends Action0To1TypeInfo[E] {

  val estimatorInfo: E = TypeUtils.instanceOfType(portO_0)

  setDefault(estimatorInfo.extractParameterMap().toSeq: _*)

  override val parameterGroups = estimatorInfo.parameterGroups

}