package com.harana.designer.backend.services.flows.argo.models

import com.harana.designer.backend.modules.projects.models.{Action, PipelineStart}
import io.circe.generic.JsonCodec

@JsonCodec
case class Pipeline(name: String,
                    start: PipelineStart,
                    actions: List[Action])