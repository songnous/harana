package com.harana.sdk.shared.models.terminals

import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class TerminalHistory(message: String, created: Instant = Instant.now)