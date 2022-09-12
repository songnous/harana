package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasMaxMemoryInMBParameter extends Parameters {

  val maxMemoryInMBParameter = IntParameter("max memory", Some("Maximum memory in MB allocated to histogram aggregation."),
    validator = RangeValidator.positiveIntegers
  )

  setDefault(maxMemoryInMBParameter, 256)
}