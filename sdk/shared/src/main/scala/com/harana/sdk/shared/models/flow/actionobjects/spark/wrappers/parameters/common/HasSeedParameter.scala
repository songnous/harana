package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{LongParameter, Parameters}

import scala.language.reflectiveCalls

trait HasSeedParameter extends Parameters {

  val seedParameter = LongParameter("seed", default = Some(0L))

}