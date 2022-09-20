package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}

import scala.language.reflectiveCalls

trait HasThresholdParameter extends Parameters {

  val thresholdParameter = DoubleParameter("threshold", validator = RangeValidator(0.0, 1.0))
  setDefault(thresholdParameter, 0.5)

}