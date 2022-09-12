package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StringType
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class StopWordsRemoverSmokeTest
    extends AbstractTransformerWrapperSmokeTest[StopWordsRemover]
    with MultiColumnTransformerWrapperTestSupport {

  def transformerWithParameters: StopWordsRemover = {
    val inPlace = NoInPlaceChoice().setOutputColumn("stopWordsRemoverOutput")
    val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection("as")).setInPlaceChoice(inPlace)

    val stopWordsRemover = new StopWordsRemover()
    stopWordsRemover.set(stopWordsRemover.singleOrMultiChoiceParameter -> single, stopWordsRemover.caseSensitiveParameter -> false)
  }

  def testValues: Seq[(Any, Any)] = {
    val inputNumbers  = Seq(Array("a", "seahorse", "The", "Horseshoe", "Crab"))
    val outputNumbers = Seq(Array("seahorse", "Horseshoe", "Crab"))
    inputNumbers.zip(outputNumbers)
  }

  def inputType: DataType = ArrayType(StringType)
  def outputType: DataType = ArrayType(StringType)
}