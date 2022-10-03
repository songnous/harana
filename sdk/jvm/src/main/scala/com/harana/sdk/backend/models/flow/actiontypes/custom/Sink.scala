package com.harana.sdk.backend.models.flow.actiontypes.custom

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.ActionTypeType1To1
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.shared.models.flow.actiontypes.custom.SinkInfo

import scala.reflect.runtime.universe.TypeTag

class Sink extends ActionTypeType1To1[DataFrame, DataFrame] with SinkInfo {

  def execute(dataFrame: DataFrame)(context: ExecutionContext) = dataFrame

  override def inferKnowledge(inputKnowledge: Knowledge[DataFrame])(context: InferContext) = (inputKnowledge, InferenceWarnings.empty)

  lazy val tTagTO_0: TypeTag[DataFrame] = typeTag
  lazy val tTagTO_1: TypeTag[DataFrame] = typeTag

}