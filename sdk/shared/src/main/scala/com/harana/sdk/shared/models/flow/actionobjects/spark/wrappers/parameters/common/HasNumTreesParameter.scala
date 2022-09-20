package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasNumTreesParameter extends Parameters {

  val numTreesParameter = IntParameter("num trees", validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))
  setDefault(numTreesParameter, 20)

}