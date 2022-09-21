package com.harana.sdk.backend.models.flow.actions.custom

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.{ActionType1To1, ExecutionContext, Knowledge}
import com.harana.sdk.shared.models.flow.actions.custom.SinkInfo

import scala.reflect.runtime.universe.TypeTag

class Sink extends ActionType1To1[DataFrame, DataFrame] with SinkInfo {

  def execute(dataFrame: DataFrame)(context: ExecutionContext) = dataFrame

  override def inferKnowledge(inputKnowledge: Knowledge[DataFrame])(context: InferContext) = (inputKnowledge, InferenceWarnings.empty)

  lazy val tTagTO_0: TypeTag[DataFrame] = typeTag
  lazy val tTagTO_1: TypeTag[DataFrame] = typeTag

}