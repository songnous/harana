package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StringType
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class StringTokenizerSmokeTest
    extends AbstractTransformerWrapperSmokeTest[StringTokenizer]
    with MultiColumnTransformerWrapperTestSupport {

  def transformerWithParameters: StringTokenizer = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("tokenized")

    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("s"))
      .setInPlaceChoice(inPlace)

    val transformer = new StringTokenizer()
    transformer.set(Seq(transformer.singleOrMultiChoiceParameter -> single): _*)
  }

  def testValues: Seq[(Any, Any)] = {
    val strings = Seq(
      "this is a test",
      "this values should be separated",
      "Bla bla bla!"
    )

    val tokenized = strings.map(_.toLowerCase.split("\\s"))
    strings.zip(tokenized)
  }

  def inputType: DataType = StringType

  def outputType: DataType = new ArrayType(StringType, true)

}
