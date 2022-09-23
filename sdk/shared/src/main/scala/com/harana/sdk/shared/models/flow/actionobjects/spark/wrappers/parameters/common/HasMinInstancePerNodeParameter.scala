package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasMinInstancePerNodeParameter extends Parameters {

  val minInstancesPerNodeParameter = IntParameter("min instances per node", validator = RangeValidator(1, Int.MaxValue, step = Some(1)))
  setDefault(minInstancesPerNodeParameter, 1)

}