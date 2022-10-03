package com.harana.sdk.shared.models.flow.actiontypes.inout

import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameters}

trait HasShouldConvertToBooleanParameter { this: Parameters =>

  val shouldConvertToBooleanParameter = BooleanParameter("convert-to-boolean", default = Some(false))
  def getShouldConvertToBoolean = $(shouldConvertToBooleanParameter)
  def setShouldConvertToBoolean(value: Boolean): this.type = set(shouldConvertToBooleanParameter, value)

}
