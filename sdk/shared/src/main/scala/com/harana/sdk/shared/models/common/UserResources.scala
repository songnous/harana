package com.harana.sdk.shared.models.common

import io.circe.generic.JsonCodec

@JsonCodec
case class UserSettings(fileSharingEnabled: Boolean = false,
												fileSharingUsername: Option[String] = None,
												fileSharingPassword: Option[String] = None,
												remoteLoginEnabled: Boolean = false,
												remoteLoginUsername: Option[String] = None,
												remoteLoginPassword: Option[String] = None,
												sshImage: Option[String] = None)