package com.harana.designer.backend.services.flows.argo.models

import com.harana.designer.backend.modules.projects.models.SBT
import io.circe.generic.JsonCodec

@JsonCodec
case class Scala(repository: Option[String],
                 path: Option[String],
                 baseImage: Option[String],
                 sbt: Option[SBT])