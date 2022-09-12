package com.harana.sdk.shared.models.common

import io.circe.generic.JsonCodec

@JsonCodec
case class HelpCategory(name: String, pages: List[HelpPage])

@JsonCodec
case class HelpPage(name: String, icon: Option[String], path: String)