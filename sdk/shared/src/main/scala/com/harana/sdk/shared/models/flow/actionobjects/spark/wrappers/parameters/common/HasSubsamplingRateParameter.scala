package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasSubsamplingRateParameter extends Parameters {

  val subsamplingRateParameter = DoubleParameter("subsampling rate", Some("The fraction of the training data used for learning each decision tree."),
    RangeValidator(0.0, 1.0, beginIncluded = false)
  )

  setDefault(subsamplingRateParameter, 1.0)
}