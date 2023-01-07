package com.harana.s3.models

import io.circe.generic.JsonCodec

@JsonCodec
case class Route(pathMatch: PathMatch,
                 destination: Destination,
                 accessPolicies: Set[AccessPolicy],
                 priority: Int,
                 immutable: Boolean,
                 durable: Boolean)