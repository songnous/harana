package com.harana.sdk.shared.components.lists

import com.harana.sdk.shared.models.common.{Component, Task}
import io.circe.generic.JsonCodec

@JsonCodec
case class TasksList(title: String,
                     icon: Option[String] = None,
                     tasks: List[Task] = List.empty) extends Component
