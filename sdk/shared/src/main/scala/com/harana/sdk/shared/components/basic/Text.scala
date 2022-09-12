package com.harana.sdk.shared.components.basic

import com.harana.sdk.shared.models.common.Component
import io.circe.generic.JsonCodec

@JsonCodec
case class Text(value: String) extends Component