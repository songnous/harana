package com.harana.sdk.backend.models.flow.utils.catalog.exceptions

import io.circe.generic.JsonCodec

@JsonCodec
case class NoParameterlessConstructorInClassError(classCanonicalName: String) extends ActionObjectCatalogError {
  val message = s"Concrete class registered in hierarchy has to have parameterless constructor ($classCanonicalName has no parameterless constructor)"
}