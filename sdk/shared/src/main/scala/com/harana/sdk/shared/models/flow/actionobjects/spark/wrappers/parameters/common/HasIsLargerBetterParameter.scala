package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameters}

import scala.language.reflectiveCalls

trait HasIsLargerBetterParameter extends Parameters {

  val isLargerBetterParameter = BooleanParameter("is-larger-better", default = Some(false))

}