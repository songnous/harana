package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasStepSizeParameter extends Parameters {

  val stepSizeDefault = 0.1

  val stepSizeParameter = DoubleParameter("step size", Some("The step size to be used for each iteration of optimization."),
    validator = RangeValidator(begin = 0.0, end = Double.MaxValue)
  )

  setDefault(stepSizeParameter, stepSizeDefault)
}