package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.backend.models.flow.actions.SqlTransformation

class SqlTransformationExample extends AbstractActionExample[SqlTransformation] {

  def action: SqlTransformation = {
    val op = new SqlTransformation()
    op.transformer.setDataFrameId("inputDF").setExpression("select avg(temp) as avg_temp, max(windspeed) as max_windspeed from inputDF")
    op.set(op.transformer.extractParameterMap())
  }

  override def fileNames = Seq("example_datetime_windspeed_hum_temp")

}
