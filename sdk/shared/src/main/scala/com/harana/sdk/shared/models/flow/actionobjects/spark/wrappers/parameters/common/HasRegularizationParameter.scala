package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}

import scala.language.reflectiveCalls

trait HasRegularizationParameter extends Parameters {

  val regularizationParameter = DoubleParameter("regularization-param", default = Some(0.0), validator = RangeValidator(0.0, Double.MaxValue))

}