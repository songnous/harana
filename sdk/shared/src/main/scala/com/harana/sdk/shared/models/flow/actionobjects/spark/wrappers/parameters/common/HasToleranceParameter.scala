package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}

import scala.language.reflectiveCalls

trait HasToleranceParameter extends Parameters {

  val toleranceDefault: Double = 1e-6
  val toleranceParameter = DoubleParameter("tolerance", default = Some(toleranceDefault), validator = RangeValidator(0.0, 1.0))

}