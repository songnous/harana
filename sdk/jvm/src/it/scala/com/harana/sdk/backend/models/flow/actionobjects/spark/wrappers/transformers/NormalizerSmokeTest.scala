package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.spark.Linalg.Vectors
import org.apache.spark.sql.types.DataType
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class NormalizerSmokeTest
    extends AbstractTransformerWrapperSmokeTest[Normalizer]
    with MultiColumnTransformerWrapperTestSupport {

  def transformerWithParameters: Normalizer = {
    val inPlace = NoInPlaceChoice().setOutputColumn("normalize")
    val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection("v")).setInPlaceChoice(inPlace)

    val transformer = new Normalizer()
    transformer.set(
      Seq(
        transformer.singleOrMultiChoiceParameter -> single,
        transformer.pParameter -> 1.0
      ): _*
    )
  }

  def testValues: Seq[(Any, Any)] = {
    val input = Seq(
      Vectors.dense(0.0, 100.0, 100.0),
      Vectors.dense(1.0, 1.0, 0.0),
      Vectors.dense(-3.0, 3.0, 0.0)
    )
    val inputAfterNormalize = Seq(
      Vectors.dense(0.0, 0.5, 0.5),
      Vectors.dense(0.5, 0.5, 0.0),
      Vectors.dense(-0.5, 0.5, 0.0)
    )
    input.zip(inputAfterNormalize)
  }

  def inputType: DataType = new com.harana.spark.Linalg.VectorUDT
  def outputType: DataType = new com.harana.spark.Linalg.VectorUDT
}
