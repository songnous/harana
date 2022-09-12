package com.harana.sdk.shared.models.common

import io.circe.generic.JsonCodec

@JsonCodec
case class SearchQuery(query: String)
