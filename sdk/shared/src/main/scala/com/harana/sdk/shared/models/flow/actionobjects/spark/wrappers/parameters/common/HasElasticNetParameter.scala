package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}

import scala.language.reflectiveCalls

trait HasElasticNetParameter extends Parameters {

  val elasticNetParameter = DoubleParameter("elastic-net-param", default = Some(0.0), validator = RangeValidator(0.0, 1.0))

}