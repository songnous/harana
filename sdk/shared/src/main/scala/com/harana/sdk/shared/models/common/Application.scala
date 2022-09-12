package com.harana.sdk.shared.models.common

import io.circe.generic.JsonCodec

@JsonCodec
case class Application(name: String, version: String)