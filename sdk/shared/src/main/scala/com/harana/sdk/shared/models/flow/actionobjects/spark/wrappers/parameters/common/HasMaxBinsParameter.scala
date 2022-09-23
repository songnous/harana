package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasMaxBinsParameter extends Parameters {

  val maxBinsParameter = IntParameter("max bins", validator = RangeValidator(2, Int.MaxValue, step = Some(1)))
  setDefault(maxBinsParameter, 32)

}