package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasMaxBinsParameter extends Parameters {

  val maxBinsParameter = IntParameter(
    name = "max bins",
    description = Some(
      "The maximum number of bins used for discretizing continuous features and for choosing how to split on features at each node. " +
      "More bins give higher granularity. Must be >= 2 and >= number of categories in any categorical feature."
    ),
    RangeValidator(2, Int.MaxValue, step = Some(1))
  )

  setDefault(maxBinsParameter, 32)
}