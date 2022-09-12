package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasMinInfoGainParameter extends Parameters {

  val minInfoGainParameter = DoubleParameter("min information gain", Some("The minimum information gain for a split to be considered at a tree node."),
    RangeValidator(0.0, Double.MaxValue)
  )

  setDefault(minInfoGainParameter, 0.0)
}