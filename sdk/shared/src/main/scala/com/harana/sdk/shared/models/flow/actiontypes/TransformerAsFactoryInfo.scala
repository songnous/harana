package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.flow.Action0To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.utils.TypeUtils

trait TransformerAsFactoryInfo[T <: TransformerInfo] extends Action0To1TypeInfo[T] {

  val transformerInfo: T = TypeUtils.actionObject(portO_0)

  override val parameterGroups = transformerInfo.parameterGroups

  setDefault(transformerInfo.extractParameterMap().toSeq: _*)

}
