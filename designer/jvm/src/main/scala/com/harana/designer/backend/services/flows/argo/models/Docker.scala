package com.harana.designer.backend.services.flows.argo.models

import io.circe.generic.JsonCodec

@JsonCodec
case class Docker(image: Option[String],
                  repository: Option[String],
                  path: Option[String])