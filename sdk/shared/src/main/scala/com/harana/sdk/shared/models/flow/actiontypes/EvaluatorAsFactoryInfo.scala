package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.flow.Action0To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.EvaluatorInfo
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import scala.reflect.runtime.universe.TypeTag

trait EvaluatorAsFactoryInfo[T <: EvaluatorInfo] extends Action0To1TypeInfo[T] {

  lazy val evaluatorInfo: T = TypeUtils.instanceOfType(portO_0)

  override val parameterGroups = evaluatorInfo.parameterGroups
  setDefault(evaluatorInfo.extractParameterMap().toSeq: _*)

}