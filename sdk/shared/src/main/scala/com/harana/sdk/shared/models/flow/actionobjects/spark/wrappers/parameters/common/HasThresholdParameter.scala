package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.spark.{Param, Params}
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasThresholdParameter extends Parameters {

  val thresholdParameter = DoubleParameter("threshold", Some("The threshold in binary classification prediction."),
    validator = RangeValidator(0.0, 1.0)
  )

  setDefault(thresholdParameter, 0.5)
}