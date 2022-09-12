package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasCheckpointIntervalParameter extends Parameters {

  val checkpointIntervalParameter = IntParameter("checkpoint interval", Some("The checkpoint interval. E.g. 10 means that the cache will get checkpointed every 10 iterations."),
    validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1))
  )

  setDefault(checkpointIntervalParameter, 10)
}