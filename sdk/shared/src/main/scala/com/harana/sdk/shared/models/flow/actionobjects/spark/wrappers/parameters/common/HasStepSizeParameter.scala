package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}

import scala.language.reflectiveCalls

trait HasStepSizeParameter extends Parameters {

  val stepSizeDefault = 0.1
  val stepSizeParameter = DoubleParameter("step-size", default = Some(stepSizeDefault), validator = RangeValidator(begin = 0.0, end = Double.MaxValue))

}