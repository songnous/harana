package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers.AssembleVector

class AssembleVectorExample extends AbstractActionExample[AssembleVector] {

  def action = {
    val op = new AssembleList.empty
    op.transformer
      .setInputColumns(Set("windspeed", "hum", "temp"))
      .setOutputColumn("assembled")
    op.set(op.transformer.extractParameterMap())
  }

  override def fileNames = Seq("example_datetime_windspeed_hum_temp")

}
