package com.harana.sdk.shared.components.widgets

import com.harana.sdk.shared.components._
import com.harana.sdk.shared.models.common.Component
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs

@JsonCodec
case class CounterStatsWidget(value: String,
            valueName: String,
            icon: Option[String] = None,
            iconPosition: Position = Position.Left,
            color: Color = Color.Default,
            colorAccent: ColorAccent = ColorAccent.Default,
            coloredBackground: Boolean = false,
            relatedValues: List[Value] = List()) extends Component
