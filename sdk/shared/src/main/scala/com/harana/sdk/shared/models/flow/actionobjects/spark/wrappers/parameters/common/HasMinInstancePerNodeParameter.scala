package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasMinInstancePerNodeParameter extends Parameters {

  val minInstancesPerNodeParameter = IntParameter(
    name = "min instances per node",
    description = Some(
      "The minimum number of instances each child must have after split. " +
        "If a split causes the left or right child to have fewer instances than the parameter's " +
        "value, the split will be discarded as invalid."
    ),
    RangeValidator(1, Int.MaxValue, step = Some(1))
  )

  setDefault(minInstancesPerNodeParameter, 1)
}