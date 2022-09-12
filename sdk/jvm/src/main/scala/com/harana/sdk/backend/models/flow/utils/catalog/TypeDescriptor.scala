package com.harana.sdk.backend.models.flow.utils.catalog

import io.circe.generic.JsonCodec

trait TypeDescriptor

@JsonCodec
case class ClassDescriptor(name: String, parent: Option[String], traits: List[String]) extends TypeDescriptor

@JsonCodec
case class TraitDescriptor(name: String, parents: List[String]) extends TypeDescriptor