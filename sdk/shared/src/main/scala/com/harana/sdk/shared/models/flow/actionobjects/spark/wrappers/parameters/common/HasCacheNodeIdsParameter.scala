package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameters}

import scala.language.reflectiveCalls

trait HasCacheNodeIdsParameter extends Parameters {

  val cacheNodeIdsParameter = BooleanParameter("cache-node-ids", default = Some(false))

}