package com.harana.designer.backend.services.flows.argo.models

import io.circe.generic.JsonCodec

@JsonCodec
case class Parameter(name: String, value: String)