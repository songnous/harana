package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasRegularizationParameter extends Parameters {

  val regularizationParameter = DoubleParameter("regularization param", Some("The regularization parameter."),
    validator = RangeValidator(0.0, Double.MaxValue)
  )

  setDefault(regularizationParameter, 0.0)
}