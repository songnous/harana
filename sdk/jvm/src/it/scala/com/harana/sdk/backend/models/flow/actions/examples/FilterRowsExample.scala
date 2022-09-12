package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.backend.models.flow.actions.FilterRows

class FilterRowsExample extends AbstractActionExample[FilterRows] {

  def action: FilterRows = {
    val op = new FilterRows()
    op.transformer.setCondition("0.4 < temp AND windspeed < 0.3")
    op.set(op.transformer.extractParameterMap())
  }

  override def fileNames = Seq("example_datetime_windspeed_hum_temp")

}
