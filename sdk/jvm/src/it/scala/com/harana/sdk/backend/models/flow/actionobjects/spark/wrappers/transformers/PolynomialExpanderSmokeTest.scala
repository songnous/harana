package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.spark.Linalg.Vectors
import org.apache.spark.sql.types.DataType
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class PolynomialExpanderSmokeTest
    extends AbstractTransformerWrapperSmokeTest[PolynomialExpander]
    with MultiColumnTransformerWrapperTestSupport {

  def transformerWithParameters: PolynomialExpander = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("polynomial")

    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("v"))
      .setInPlaceChoice(inPlace)

    val transformer = new PolynomialExpander()
    transformer.set(
      Seq(
        transformer.singleOrMultiChoiceParameter -> single,
        transformer.degreeParameter -> 3
      ): _*
    )
  }

  def testValues: Seq[(Any, Any)] = {
    val input         = Seq(
      Vectors.dense(1.0),
      Vectors.dense(1.0, 2.0)
    )
    val inputAfterDCT = Seq(
      // x, x^2, x^3
      Vectors.dense(1.0, 1.0, 1.0),
      // x, x^2, x^3, y, x * y, x^2 * y, x * y^2, y^2, y^3
      Vectors.dense(1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 4.0, 4.0, 8.0)
    )
    input.zip(inputAfterDCT)
  }

  def inputType: DataType = new com.harana.spark.Linalg.VectorUDT

  def outputType: DataType = new com.harana.spark.Linalg.VectorUDT
}
