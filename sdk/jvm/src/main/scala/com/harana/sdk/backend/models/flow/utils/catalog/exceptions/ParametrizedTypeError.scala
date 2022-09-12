package com.harana.sdk.backend.models.flow.utils.catalog.exceptions

import io.circe.generic.JsonCodec

@JsonCodec
case class ParametrizedTypeError(parameterizedType: String) extends ActionObjectCatalogError {
  val message = s"Cannot register parametrized type in hierarchy (Type $parameterizedType is parametrized)"
}