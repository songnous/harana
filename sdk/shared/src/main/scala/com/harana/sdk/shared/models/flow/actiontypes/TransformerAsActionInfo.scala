package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.flow.Action1To2TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.catalog.Catalog
import com.harana.sdk.shared.models.flow.utils.TypeUtils
import izumi.reflect.Tag

trait TransformerAsActionInfo[T <: TransformerInfo] extends Action1To2TypeInfo[DataFrameInfo, DataFrameInfo, T] {

  lazy val transformerInfo: T = TypeUtils.actionObject(portO_1)

  override val parameterGroups = transformerInfo.parameterGroups

  setDefault(transformerInfo.extractParameterMap().toSeq: _*)

  lazy val portI_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]
  lazy val portO_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

}