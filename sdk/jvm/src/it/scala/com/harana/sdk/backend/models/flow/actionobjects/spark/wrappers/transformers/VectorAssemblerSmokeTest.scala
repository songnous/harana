package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.parameters.selections.{MultipleColumnSelection, NameColumnSelection}

class VectorAssemblerSmokeTest extends AbstractTransformerWrapperSmokeTest[VectorAssembler] {

  def transformerWithParameters: VectorAssembler = {
    val transformer = new VectorAssembler()
    transformer.set(
      Seq(
        transformer.inputColumnsParameter -> MultipleColumnSelection(List(NameColumnSelection(Set("i", "i2")))),
        transformer.outputColumnParameter -> "outputVector"
      ): _*
    )
  }
}
