package com.harana.sdk.backend.models.flow.actions.custom

import com.harana.sdk.backend.models.flow.{Action0To1, ExecutionContext}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow.actions.custom.SourceInfo

import scala.reflect.runtime.universe.TypeTag

class Source extends Action0To1[DataFrame] with SourceInfo {

  def execute()(context: ExecutionContext) = throw new IllegalStateException("should not be executed")

  lazy val tTagTO_0: TypeTag[DataFrame] = typeTag
  lazy val tTagTO_1: TypeTag[DataFrame] = typeTag

}