package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}

import scala.language.reflectiveCalls

trait HasNumberOfClustersParameter extends Parameters {

  val kParameter = IntParameter("k", default = Some(2), validator = RangeValidator(begin = 2, end = Int.MaxValue, step = Some(1)))

}