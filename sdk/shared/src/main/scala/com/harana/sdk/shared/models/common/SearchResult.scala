package com.harana.sdk.shared.models.common

import io.circe.generic.JsonCodec

@JsonCodec
case class SearchResult[T <: Entity](entities: List[T],
                                     referredEntities: Map[String, T],
                                     scores: List[Double])