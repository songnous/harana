package com.harana.sdk.shared.components.widgets

import com.harana.sdk.shared.components.Value
import com.harana.sdk.shared.models.common.Component
import io.circe.generic.JsonCodec

@JsonCodec
case class DonutWidget(title: String,
            subtitle: String,
            half: Boolean,
            values: List[Value],
            relatedValues: List[Value] = List()) extends Component
