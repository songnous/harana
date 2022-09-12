package com.harana.designer.backend.services.flows.argo.models

import io.circe.generic.JsonCodec

@JsonCodec
case class CanaryStep(weight: Int,
                      pause: Option[Int])
