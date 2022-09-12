package com.harana.sdk.shared.models.flow.exceptions

import io.circe.generic.JsonCodec

@JsonCodec
case class NoParameterlessConstructorInActionError(actionType: String) extends ActionsCatalogError {
  val message = s"Registered Action has to have parameterless constructor (Action $actionType has no parameterless constructor)"
}