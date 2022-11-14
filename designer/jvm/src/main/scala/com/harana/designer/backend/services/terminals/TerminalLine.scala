package com.harana.designer.backend.services.terminals

import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class TerminalLine(time: Instant, line: String)