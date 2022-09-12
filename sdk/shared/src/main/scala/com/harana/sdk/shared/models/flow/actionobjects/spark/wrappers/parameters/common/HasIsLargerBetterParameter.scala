package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameters}

import scala.language.reflectiveCalls

trait HasIsLargerBetterParameter extends Parameters {

  val isLargerBetterParameter = BooleanParameter(
    name = "is larger better",
    description = Some("Indicates whether the returned metric is better to be maximized or minimized.")
  )

  setDefault(isLargerBetterParameter -> false)
}
