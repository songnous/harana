package com.harana.sdk.backend.models.flow.actiontypes.read

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actiontypes.ActionTypeType0To1
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.HaranaIOError
import com.harana.sdk.shared.models.flow.actiontypes.read.ReadTransformerInfo

import izumi.reflect.Tag
import java.io._

class ReadTransformer extends ActionTypeType0To1[Transformer] with ReadTransformerInfo {

  def execute()(context: ExecutionContext) = {
    val path = getSourcePath
    try
      Transformer.load(context, path)
    catch {
      case e: IOException => throw HaranaIOError(e).toException
    }
  }

  lazy val tTagTO_0: Tag[Transformer] = typeTag
}