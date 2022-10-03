package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}

import scala.language.reflectiveCalls

trait HasMaxDepthParameter extends Parameters {

  val maxDepthParameter = IntParameter("max-depth", default = Some(5), validator = RangeValidator(0, 30, step = Some(1)))

}