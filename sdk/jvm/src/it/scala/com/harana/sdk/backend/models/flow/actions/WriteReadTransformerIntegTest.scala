package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actions.write.WriteTransformer
import com.harana.sdk.shared.models.designer.flow.actions.read.ReadTransformer

abstract class WriteReadTransformerIntegTest extends IntegratedTestSupport {

  def writeReadTransformer(transformer: Transformer, outputFile: String) = {
    val writeTransformer = WriteTransformer(outputFile).setShouldOverwrite(true)
    writeTransformer.executeUntyped(List(transformer))(executionContext)

    val deserializedTransformer = ReadTransformer(outputFile).executeUntyped(List.empty)(executionContext).head
    deserializedTransformer shouldBe transformer
  }

  def writeTransformer(transformer: Transformer, outputFile: String, overwrite: Boolean) = {
    val writeTransformer = WriteTransformer(outputFile).setShouldOverwrite(overwrite)
    writeTransformer.executeUntyped(List(transformer))(executionContext)
  }
}
