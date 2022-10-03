package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.RTransformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.RTransformation
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo

class RTransformationExample extends AbstractActionExample[RTransformation] {

  // This is mocked because R executor is not available in tests.
  class RTransformationMock extends RTransformation {

    override def execute(arg: DataFrame)(context: ExecutionContext) = (PythonTransformationExample.execute(arg)(context), mock[RTransformer])

  }

  def action: RTransformation = {
    val op = new RTransformationMock()
    op.transformer
      .setCodeParameter(
        "transform <- function(dataframe) {" +
          "\n  filtered_df <- filter(dataframe, dataframe$temp > 0.4)" +
          "\n  sorted_filtered_df <- orderBy(filtered_df, desc(filtered_df$windspeed))" +
          "\n  return(sorted_filtered_df)" +
          "\n}"
      )
    op.set(op.transformer.extractParameterMap())

  }

  override def fileNames = Seq("example_datetime_windspeed_hum_temp")

}
