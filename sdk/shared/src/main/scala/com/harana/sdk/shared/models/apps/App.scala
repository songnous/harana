package com.harana.sdk.shared.models.apps

import java.time.Instant

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Background, Entity, Status, User, Visibility}
import App.AppId
import com.harana.sdk.shared.utils.Random
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._

@JsonCodec
case class App(title: String,
               description: String,
               category: String,
               image: String,
               httpPort: Int,
               websocketPort: Option[Int],
               createdBy: Option[UserId],
               created: Instant,
               updatedBy: Option[UserId],
               updated: Instant,
               id: AppId,
               status: Status,
               visibility: Visibility,
               version: Long,
               background: Option[Background],
               tags: Set[String],
               relationships: Map[String, EntityId])
  extends Entity with Serializable {
  type EntityType = App
}

object App {
  type AppId = String

  def apply(title: String,
            description: String,
            category: String,
            image: String,
            httpPort: Int,
            websocketPort: Option[Int],
            createdBy: Option[UserId],
            visibility: Visibility,
            background: Option[Background],
            tags: Set[String]): App = {
    apply(title, description, category, image, httpPort, websocketPort, createdBy, Instant.now, createdBy, Instant.now, Random.long, Status.Active, visibility, 1L, background, tags, Map())
  }
}