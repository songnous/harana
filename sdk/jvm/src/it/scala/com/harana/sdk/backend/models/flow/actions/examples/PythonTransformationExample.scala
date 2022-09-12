package com.harana.sdk.backend.models.flow.actions.examples

import org.apache.spark.sql.functions._
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.PythonTransformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.PythonTransformation
import com.harana.sdk.shared.models.flow.ActionObjectInfo

class PythonTransformationExample extends AbstractActionExample[PythonTransformation] {

  // This is mocked because Python executor is not available in tests.
  class PythonTransformationMock extends PythonTransformation {

    override def execute(arg: DataFrame)(context: ExecutionContext) = (PythonTransformationExample.execute(arg)(context), mock[PythonTransformer])

  }

  def action: PythonTransformation = {
    val op = new PythonTransformationMock()
    op.transformer.setCodeParameter(
        "def transform(df):" +
          "\n    return df.filter(df.temp > 0.4).sort(df.windspeed, ascending=False)"
      )
    op.set(op.transformer.extractParameterMap())

  }

  override def fileNames = Seq("example_datetime_windspeed_hum_temp")

}

object PythonTransformationExample {

  def execute(arg: DataFrame)(context: ExecutionContext) = {
    val resultSparkDataFrame = arg.sparkDataFrame.filter("temp > 0.4").sort(desc("windspeed"))
    DataFrame.fromSparkDataFrame(resultSparkDataFrame)
  }
}
