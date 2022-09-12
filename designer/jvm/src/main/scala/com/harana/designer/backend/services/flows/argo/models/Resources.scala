package com.harana.designer.backend.services.flows.argo.models

import io.circe.generic.JsonCodec

@JsonCodec
case class Resources(cpu: Option[String],
                     memory: Option[String])