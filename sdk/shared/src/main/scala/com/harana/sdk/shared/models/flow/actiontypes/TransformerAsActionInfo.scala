package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.flow.Action1To2TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import scala.reflect.runtime.universe.TypeTag

trait TransformerAsActionInfo[T <: TransformerInfo] extends Action1To2TypeInfo[DataFrameInfo, DataFrameInfo, T] {

  lazy val transformerInfo: T = TypeUtils.instanceOfType(portO_1)

  override val parameterGroups = transformerInfo.parameterGroups

  setDefault(transformerInfo.extractParameterMap().toSeq: _*)

  lazy val portI_0: TypeTag[DataFrameInfo] = typeTag[DataFrameInfo]
  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag[DataFrameInfo]
}