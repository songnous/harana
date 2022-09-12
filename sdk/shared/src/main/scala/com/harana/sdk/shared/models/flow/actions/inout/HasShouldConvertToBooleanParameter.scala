package com.harana.sdk.shared.models.flow.actions.inout

import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameters}

trait HasShouldConvertToBooleanParameter {
  this: Parameters =>

  val shouldConvertToBooleanParameter = BooleanParameter("convert to boolean", Some("Should columns containing only 0 and 1 be converted to Boolean?"))
  setDefault(shouldConvertToBooleanParameter, false)

  def getShouldConvertToBoolean = $(shouldConvertToBooleanParameter)

  def setShouldConvertToBoolean(value: Boolean): this.type = set(shouldConvertToBooleanParameter, value)

}
