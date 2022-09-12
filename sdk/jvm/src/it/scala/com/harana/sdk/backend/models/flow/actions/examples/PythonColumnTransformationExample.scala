package com.harana.sdk.backend.models.flow.actions.examples

import org.apache.spark.sql.functions.when
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.PythonColumnTransformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.PythonColumnTransformation
import com.harana.sdk.shared.models.flow.actionobjects.TargetTypeChoices
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class PythonColumnTransformationExample extends AbstractActionExample[PythonColumnTransformation] {

  val inputColumnName = "Weight"
  val outputColumnName = "WeightCutoff"

  // This is mocked because Python executor is not available in tests.
  class PythonColumnTransformationMock extends PythonColumnTransformation {

    override def execute(arg: DataFrame)(context: ExecutionContext): (DataFrame, PythonColumnTransformer) = {
      val sdf = arg.sparkDataFrame
      val resultSparkDataFrame = sdf.select(
        sdf("*"),
        when(sdf(inputColumnName) > 2.0, 2.0)
          .otherwise(sdf(inputColumnName))
          .alias(outputColumnName)
      )
      (DataFrame.fromSparkDataFrame(resultSparkDataFrame), mock[PythonColumnTransformer])
    }

  }

  def action: PythonColumnTransformation = {
    val op = new PythonColumnTransformationMock()
    val inPlace = NoInPlaceChoice().setOutputColumn(s"$outputColumnName")
    val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection(inputColumnName)).setInPlaceChoice(inPlace)
    op.transformer.setTargetType(TargetTypeChoices.DoubleTargetTypeChoice()).setSingleOrMultiChoice(single).setCodeParameter(
        "def transform_value(value, column_name):\n" +
          "    return min(value, 2.0)"
      )
    op.set(op.transformer.extractParameterMap())
  }

  override def fileNames = Seq("example_animals")

}
