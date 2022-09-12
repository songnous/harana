package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StringType
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class RegexTokenizerSmokeTest
    extends AbstractTransformerWrapperSmokeTest[RegexTokenizer]
    with MultiColumnTransformerWrapperTestSupport {

  def transformerWithParameters: RegexTokenizer = {
    val inPlace = NoInPlaceChoice().setOutputColumn("tokenized")
    val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection("s")).setInPlaceChoice(inPlace)

    val transformer = new RegexTokenizer()
    transformer.set(
      Seq(
        transformer.singleOrMultiChoiceParameter -> single,
        transformer.gapsParameter -> false,
        transformer.minTokenLengthParameter -> 1,
        transformer.patternParameter -> "\\d+"
      ): _*
    )
  }

  def testValues: Seq[(Any, Any)] = {
    val strings = Seq(
      "100 200 300",
      "400 500 600",
      "700 800 900"
    )

    val tokenized = strings.map(_.toLowerCase.split(" "))
    strings.zip(tokenized)
  }

  def inputType: DataType = StringType
  def outputType: DataType = new ArrayType(StringType, true)
}
