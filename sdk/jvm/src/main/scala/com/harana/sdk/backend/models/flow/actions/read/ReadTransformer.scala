package com.harana.sdk.backend.models.flow.actions.read

import com.harana.sdk.backend.models.flow.{ActionType0To1, ExecutionContext}
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actions.exceptions.HaranaIOError
import com.harana.sdk.shared.models.flow.actions.read.ReadTransformerInfo

import scala.reflect.runtime.universe.TypeTag
import java.io._

class ReadTransformer extends ActionType0To1[Transformer] with ReadTransformerInfo {

  def execute()(context: ExecutionContext) = {
    val path = getSourcePath
    try
      Transformer.load(context, path)
    catch {
      case e: IOException => throw HaranaIOError(e).toException
    }
  }

  lazy val tTagTO_0: TypeTag[Transformer] = typeTag
}