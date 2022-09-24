package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameters}

import scala.language.reflectiveCalls

trait HasStandardizationParameter extends Parameters {

  val standardizationParameter = BooleanParameter("standardization", default = Some(true))

}