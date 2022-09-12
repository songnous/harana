package com.harana.sdk.shared.models.schedules

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Entity, ParameterValue, Status, Visibility}
import com.harana.sdk.shared.models.schedules.Notifier.NotifierId
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.utils.Random
import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class Notifier(title: String,
                    description: String,
                    `type`: NotifierType,
                    parameterValues: Map[ParameterName, ParameterValue],
                    createdBy: Option[UserId],
                    created: Instant,
                    updatedBy: Option[UserId],
                    updated: Instant,
                    id: NotifierId,
                    status: Status,
                    visibility: Visibility,
                    version: Long,
                    tags: Set[String],
                    relationships: Map[String, EntityId])
  extends Entity with Serializable {

  type EntityType = Notifier
}

object Notifier {
  type NotifierId = String

  def apply(title: String,
            description: String,
            `type`: NotifierType,
            parameterValues: Map[ParameterName, ParameterValue],
            createdBy: Option[UserId],
            visibility: Visibility): Notifier =
    apply(title, description, `type`, parameterValues, createdBy, Instant.now, createdBy, Instant.now, Random.long, Status.Active, visibility, 1L, Set(), Map())
}