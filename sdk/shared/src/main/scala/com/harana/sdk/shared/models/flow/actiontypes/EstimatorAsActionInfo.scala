package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.flow.Action1To2TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, EstimatorInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.catalog.Catalog
import com.harana.sdk.shared.models.flow.utils.TypeUtils
import izumi.reflect.Tag

trait EstimatorAsActionInfo[E <: EstimatorInfo, T <: TransformerInfo]
  extends Action1To2TypeInfo[DataFrameInfo, DataFrameInfo, T] {

  val tTagInfoE: Tag[E]

  lazy val estimatorInfo: E = TypeUtils.actionObject(tTagInfoE)

  override val parameterGroups = estimatorInfo.parameterGroups

  setDefault(estimatorInfo.extractParameterMap().toSeq: _*)

  private def estimatorWithParameters = {
    val estimatorWithParameters = estimatorInfo.set(extractParameterMap())
    validateDynamicParameters(estimatorWithParameters)
    estimatorWithParameters
  }

  lazy val portI_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]
  lazy val portO_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

}
