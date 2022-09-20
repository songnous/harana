package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasMaxIterationsParameter extends Parameters {

  val maxIterationsDefault: Int = 10
  val maxIterationsParameter = IntParameter("max iterations", validator = RangeValidator.positiveIntegers)
  setDefault(maxIterationsParameter, maxIterationsDefault)

}
