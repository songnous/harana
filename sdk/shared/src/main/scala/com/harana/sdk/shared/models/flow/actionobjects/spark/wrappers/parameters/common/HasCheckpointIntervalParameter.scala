package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}

import scala.language.reflectiveCalls

trait HasCheckpointIntervalParameter extends Parameters {

  val checkpointIntervalParameter = IntParameter("checkpoint-interval", default = Some(10), validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))

}