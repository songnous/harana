package com.harana.designer.backend.services.flows.argo.models

import com.harana.designer.backend.modules.projects.models.CanaryStep
import io.circe.generic.JsonCodec

@JsonCodec
case class Canary(stableService: String,
                  canaryService: String,
                  steps: List[CanaryStep] = List(),
                  maxSurge: Option[String] = None,
                  maxUnavailable: Option[String] = None)