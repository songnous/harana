package com.harana.designer.backend.services.flows.argo.models

import com.harana.designer.backend.modules.projects.models.{BlueGreen, Canary}
import io.circe.generic.JsonCodec

@JsonCodec
case class Strategy(blueGreen: Option[BlueGreen],
                    canary: Option[Canary])