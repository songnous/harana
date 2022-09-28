package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.flow.Action0To1Info
import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import scala.reflect.runtime.universe.TypeTag

trait EstimatorAsFactoryInfo[E <: EstimatorInfo] extends Action0To1Info[E] {

  val estimatorInfo: E = TypeUtils.instanceOfType(portO_0)

  setDefault(estimatorInfo.extractParameterMap().toSeq: _*)

  override val parameterGroups = estimatorInfo.parameterGroups

}