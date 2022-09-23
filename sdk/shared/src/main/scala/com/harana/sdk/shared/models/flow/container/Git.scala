package com.harana.sdk.shared.models.designer.flow.container

import io.circe.generic.JsonCodec

@JsonCodec
case class Git(url: String,
               path: Option[String],
               branch: Option[String],
               tag: Option[String],
               commit: Option[String],
               username: Option[String],
               password: Option[String],
               oauthToken: Option[String])