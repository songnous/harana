package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers.Binarize

class BinarizeExample extends AbstractActionExample[Binarize] {

  def action = {
    val op = new Binarize()
    op.transformer.setSingleColumn("hum", "hum_bin").setThreshold(0.5)
    op.set(op.transformer.extractParameterMap())
  }

  override def fileNames = Seq("example_datetime_windspeed_hum_temp")

}
