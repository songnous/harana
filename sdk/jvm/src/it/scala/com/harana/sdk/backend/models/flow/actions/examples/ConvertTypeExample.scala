package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.shared.models.flow.actionobjects.TargetTypeChoices.IntegerTargetTypeChoice
import com.harana.sdk.backend.models.flow.actions.ConvertType

class ConvertTypeExample extends AbstractActionExample[ConvertType] {

  def action: ConvertType = {
    val op = new ConvertType()
    op.transformer.setSingleColumn("beds", "beds_int")
    op.transformer.setTargetType(IntegerTargetTypeChoice())
    op.set(op.transformer.extractParameterMap())
  }

  override def fileNames = Seq("example_city_beds_price")

}
