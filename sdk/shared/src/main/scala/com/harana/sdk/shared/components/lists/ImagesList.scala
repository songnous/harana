package com.harana.sdk.shared.components.lists

import com.harana.sdk.shared.models.common.{Component, Image}
import io.circe.generic.JsonCodec

@JsonCodec
case class ImagesList(title: String,
                      icon: Option[String] = None,
                      images: List[Image] = List.empty,
                      columns: Int) extends Component
