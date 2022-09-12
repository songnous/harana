package com.harana.sdk.shared.components.cards.search

import com.harana.sdk.shared.models.common.Component
import io.circe.generic.JsonCodec

@JsonCodec
case class LatestSearchesCard(latestSearches: List[LatestSearch] = List()) extends Component

@JsonCodec
case class LatestSearch(title: String, subtitle: String)
