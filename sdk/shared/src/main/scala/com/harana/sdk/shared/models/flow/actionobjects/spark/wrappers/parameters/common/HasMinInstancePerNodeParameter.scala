package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}

import scala.language.reflectiveCalls

trait HasMinInstancePerNodeParameter extends Parameters {

  val minInstancesPerNodeParameter = IntParameter("min-instances-per-node", default = Some(1), validator = RangeValidator(1, Int.MaxValue, step = Some(1)))

}