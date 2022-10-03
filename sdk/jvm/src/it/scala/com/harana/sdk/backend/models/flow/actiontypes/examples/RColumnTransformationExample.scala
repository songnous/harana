package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.RColumnTransformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.RColumnTransformation
import com.harana.sdk.shared.models.flow.actionobjects.{ActionObjectInfo, TargetTypeChoices}
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class RColumnTransformationExample extends AbstractActionExample[RColumnTransformation] {

  val poundInKg = 0.45359237
  val inputColumnName = "Weight"
  val outputColumnName = "WeightInPounds"

  // This is mocked because R executor is not available in tests.
  class RColumnTransformationMock extends RColumnTransformation {

    override def execute(arg: DataFrame)(context: ExecutionContext): (DataFrame, RColumnTransformer) = {
      val sdf = arg.sparkDataFrame
      val resultSparkDataFrame = sdf.select(sdf("*"), (sdf(inputColumnName) / poundInKg).alias(outputColumnName))
      (DataFrame.fromSparkDataFrame(resultSparkDataFrame), mock[RColumnTransformer])
    }

  }

  def action: RColumnTransformation = {
    val o = new RColumnTransformationMock()

    val inPlace = NoInPlaceChoice().setOutputColumn(s"$outputColumnName")
    val single  = SingleColumnChoice().setInputColumn(NameSingleColumnSelection(inputColumnName)).setInPlaceChoice(inPlace)
    o.transformer.setTargetType(TargetTypeChoices.DoubleTargetTypeChoice()).setSingleOrMultiChoice(single).setCodeParameter(
        "transform.column <- function(column, column.name) {" +
          s"\n  return(column / $poundInKg)" +
          "\n}"
      )
    o.set(o.transformer.extractParameterMap())
  }

  override def fileNames = Seq("example_animals")

}
