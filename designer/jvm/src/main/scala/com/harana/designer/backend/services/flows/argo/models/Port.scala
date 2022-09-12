package com.harana.designer.backend.services.flows.argo.models

import io.circe.generic.JsonCodec

@JsonCodec
case class Port(name: String,
                internal: Option[Int],
                external: Option[Int])