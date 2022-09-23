package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.flow.Action0To1Info
import com.harana.sdk.shared.models.flow.actionobjects.EvaluatorInfo
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import scala.reflect.runtime.universe.TypeTag

trait EvaluatorAsFactoryInfo[T <: EvaluatorInfo] extends Action0To1Info[T] {

  lazy val evaluatorInfo: T = TypeUtils.instanceOfType(portO_0)

  override val parameters = evaluatorInfo.parameters
  setDefault(evaluatorInfo.extractParameterMap().toSeq: _*)

}