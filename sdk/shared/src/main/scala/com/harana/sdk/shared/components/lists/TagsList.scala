package com.harana.sdk.shared.components.lists

import com.harana.sdk.shared.models.common.Component
import io.circe.generic.JsonCodec

@JsonCodec
case class TagsList(title: String,
                    icon: Option[String] = None,
                    tags: List[String] = List.empty) extends Component
