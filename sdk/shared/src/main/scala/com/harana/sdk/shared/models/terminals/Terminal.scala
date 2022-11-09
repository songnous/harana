package com.harana.sdk.shared.models.terminals

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Background, Entity, Status, Visibility}
import com.harana.sdk.shared.models.terminals.Terminal.TerminalId
import com.harana.sdk.shared.utils.Random
import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class Terminal(title: String,
                    description: String,
                    createdBy: Option[UserId],
                    created: Instant,
                    updatedBy: Option[UserId],
                    updated: Instant,
                    id: TerminalId,
                    image: String,
                    shell: String,
                    history: List[TerminalHistory],
                    paused: Boolean,
                    status: Status,
                    visibility: Visibility,
                    version: Long,
                    background: Option[Background],
                    tags: Set[String],
                    relationships: Map[String, EntityId])
  extends Entity with Serializable {

  type EntityType = Terminal
}

object Terminal {
  type TerminalId = String

  def apply(title: String,
            description: String,
            createdBy: UserId,
            image: String,
            shell: String): Terminal = {
    apply(title, description, Some(createdBy), Instant.now, Some(createdBy), Instant.now, Random.long, image, shell, List(), false, Status.Active, Visibility.Owner, 1L, None, Set(), Map())
  }
}