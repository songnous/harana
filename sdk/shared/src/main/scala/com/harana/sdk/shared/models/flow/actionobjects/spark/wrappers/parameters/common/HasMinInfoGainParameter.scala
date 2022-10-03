package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}

import scala.language.reflectiveCalls

trait HasMinInfoGainParameter extends Parameters {

  val minInfoGainParameter = DoubleParameter("min-information-gain", default = Some(0.0), validator = RangeValidator(0.0, Double.MaxValue))

}