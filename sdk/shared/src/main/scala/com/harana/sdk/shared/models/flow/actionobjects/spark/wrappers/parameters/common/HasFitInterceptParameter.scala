package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameters}

import scala.language.reflectiveCalls

trait HasFitInterceptParameter extends Parameters {

  val fitInterceptParameter = BooleanParameter("fit intercept", Some("Whether to fit an intercept term."))

  setDefault(fitInterceptParameter, true)
}