package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}

import scala.language.reflectiveCalls

trait HasMaxMemoryInMBParameter extends Parameters {

  val maxMemoryInMBParameter = IntParameter("max-memory", default = Some(256), validator = RangeValidator.positiveIntegers)

}