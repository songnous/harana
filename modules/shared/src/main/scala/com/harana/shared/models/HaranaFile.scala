package com.harana.shared.models

import java.time.Instant

import io.circe.generic.JsonCodec

@JsonCodec
case class HaranaFile(name: String,
                      path: String,
                      extension: Option[String],
                      isFolder: Boolean,
                      created: Instant,
                      updated: Instant,
                      size: Long,
                      tags: List[String])