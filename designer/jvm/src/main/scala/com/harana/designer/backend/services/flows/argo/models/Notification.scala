package com.harana.designer.backend.services.flows.argo.models

import com.harana.designer.backend.modules.projects.models.{Email, NotificationSlack}
import io.circe.generic.JsonCodec

@JsonCodec
case class Notification(name: String,
                        email: Option[Email],
                        slack: Option[NotificationSlack])
