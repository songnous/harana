package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}

import scala.language.reflectiveCalls

trait HasNumTreesParameter extends Parameters {

  val numTreesParameter = IntParameter("num-trees", default = Some(20), validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))

}