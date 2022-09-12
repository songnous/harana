package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.spark.Linalg.Vectors
import org.apache.spark.sql.types.DataType
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class DiscreteCosineTransformerSmokeTest
    extends AbstractTransformerWrapperSmokeTest[DiscreteCosineTransformer]
    with MultiColumnTransformerWrapperTestSupport {

  def transformerWithParameters: DiscreteCosineTransformer = {
    val inPlace = NoInPlaceChoice().setOutputColumn("dct")
    val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection("v")).setInPlaceChoice(inPlace)

    val transformer = new DiscreteCosineTransformer()
    transformer.set(
      Seq(
        transformer.singleOrMultiChoiceParameter -> single,
        transformer.inverseParameter -> false
      ): _*
    )
  }

  def testValues: Seq[(Any, Any)] = {
    val input = Seq(
      Vectors.dense(0.0),
      Vectors.dense(1.0),
      Vectors.dense(2.0)
    )
    val inputAfterDCT = Seq(
      Vectors.dense(0.0),
      Vectors.dense(1.0),
      Vectors.dense(2.0)
    )
    input.zip(inputAfterDCT)
  }

  def inputType: DataType = new com.harana.spark.Linalg.VectorUDT

  def outputType: DataType = new com.harana.spark.Linalg.VectorUDT

}
