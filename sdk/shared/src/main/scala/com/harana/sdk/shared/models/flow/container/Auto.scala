package com.harana.sdk.shared.models.designer.flow.container

import io.circe.generic.JsonCodec

@JsonCodec
case class Auto(repository: Option[String],
                path: Option[String],
                builder: Option[String])