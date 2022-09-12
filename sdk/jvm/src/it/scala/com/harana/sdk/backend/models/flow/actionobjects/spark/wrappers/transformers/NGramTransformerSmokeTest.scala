package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StringType
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class NGramTransformerSmokeTest
    extends AbstractTransformerWrapperSmokeTest[NGramTransformer]
    with MultiColumnTransformerWrapperTestSupport {

  def transformerWithParameters: NGramTransformer = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("ngrams")

    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("as"))
      .setInPlaceChoice(inPlace)

    val transformer = new NGramTransformer()
    transformer.set(
      Seq(
        transformer.singleOrMultiChoiceParameter -> single,
        transformer.nParameter -> 2
      ): _*
    )
  }

  def testValues: Seq[(Any, Any)] = {
    val strings = Seq(
      Array("a", "b", "c"),
      Array("d", "e", "f")
    )

    val ngrams = Seq(
      Array("a b", "b c"),
      Array("d e", "e f")
    )
    strings.zip(ngrams)
  }

  def inputType: DataType = new ArrayType(StringType, true)

  def outputType: DataType = new ArrayType(StringType, false)

}
