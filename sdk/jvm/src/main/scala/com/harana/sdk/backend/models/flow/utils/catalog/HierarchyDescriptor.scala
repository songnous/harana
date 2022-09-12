package com.harana.sdk.backend.models.flow.utils.catalog

import io.circe.generic.JsonCodec

@JsonCodec
case class HierarchyDescriptor(traits: Map[String, TraitDescriptor], classes: Map[String, ClassDescriptor])
