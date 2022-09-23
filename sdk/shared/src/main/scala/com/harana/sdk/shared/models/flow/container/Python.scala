package com.harana.sdk.shared.models.designer.flow.container

import io.circe.generic.JsonCodec

@JsonCodec
case class Python(repository: Option[String],
                  path: Option[String],
                  file: Option[String],
                  baseImage: Option[String])