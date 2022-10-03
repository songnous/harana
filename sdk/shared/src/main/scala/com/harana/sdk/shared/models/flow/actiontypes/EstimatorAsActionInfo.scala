package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.flow.Action1To2TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, EstimatorInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import scala.reflect.runtime.universe.TypeTag

trait EstimatorAsActionInfo[E <: EstimatorInfo, T <: TransformerInfo]
  extends Action1To2TypeInfo[DataFrameInfo, DataFrameInfo, T] {

  val tTagInfoE: TypeTag[E]

  lazy val estimatorInfo: E = TypeUtils.instanceOfType(tTagInfoE)

  override val parameterGroups = estimatorInfo.parameterGroups

  setDefault(estimatorInfo.extractParameterMap().toSeq: _*)

  private def estimatorWithParameters = {
    val estimatorWithParameters = estimatorInfo.set(extractParameterMap())
    validateDynamicParameters(estimatorWithParameters)
    estimatorWithParameters
  }

  lazy val portI_0: TypeTag[DataFrameInfo] = typeTag[DataFrameInfo]
  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag[DataFrameInfo]

}
