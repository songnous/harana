package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.flow.Action1To2Info
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import scala.reflect.runtime.universe.TypeTag

trait TransformerAsActionInfo[T <: TransformerInfo] extends Action1To2Info[DataFrameInfo, DataFrameInfo, T] {

  lazy val transformerInfo: T = TypeUtils.instanceOfType(portO_1)

  override val parameterGroups = transformerInfo.parameterGroups

  setDefault(transformerInfo.extractParameterMap().toSeq: _*)

  lazy val portI_0: TypeTag[DataFrameInfo] = typeTag[DataFrameInfo]
  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag[DataFrameInfo]
}