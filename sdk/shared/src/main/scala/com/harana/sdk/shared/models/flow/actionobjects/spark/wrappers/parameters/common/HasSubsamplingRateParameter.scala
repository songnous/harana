package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}

import scala.language.reflectiveCalls

trait HasSubsamplingRateParameter extends Parameters {

  val subsamplingRateParameter = DoubleParameter("subsampling rate", validator = RangeValidator(0.0, 1.0, beginIncluded = false))
  setDefault(subsamplingRateParameter, 1.0)

}