package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.DoubleType
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class BinarizerSmokeTest
    extends AbstractTransformerWrapperSmokeTest[Binarizer]
    with MultiColumnTransformerWrapperTestSupport {

  def transformerWithParameters: Binarizer = {
    val inPlace = NoInPlaceChoice().setOutputColumn("binarizerOutput")
    val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection("d")).setInPlaceChoice(inPlace)

    val binarizer = new Binarizer()
    binarizer.set(binarizer.singleOrMultiChoiceParameter -> single, binarizer.thresholdParameter -> 0.5)
  }

  def testValues: Seq[(Any, Any)] = {
    val inputNumbers  = Seq(0.2, 0.5, 1.8)
    val outputNumbers = Seq(0.0, 0.0, 1.0)
    inputNumbers.zip(outputNumbers)
  }

  def inputType: DataType = DoubleType
  def outputType: DataType = DoubleType
}
