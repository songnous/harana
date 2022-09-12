package com.harana.designer.backend.services.flows.argo.models

import com.harana.designer.backend.modules.projects.models.Trigger
import io.circe.generic.JsonCodec

@JsonCodec
case class DaemonStart(triggers: Option[List[Trigger]])