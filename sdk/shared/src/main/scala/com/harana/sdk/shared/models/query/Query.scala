package com.harana.sdk.shared.models.query

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Entity, Status, Visibility}
import Query.QueryId
import com.harana.sdk.shared.utils.Random
import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class Query(title: String,
                 description: String,
                 query: String,
                 createdBy: Option[UserId],
                 created: Instant,
                 updatedBy: Option[UserId],
                 updated: Instant,
                 id: QueryId,
                 status: Status,
                 visibility: Visibility,
                 version: Long,
                 tags: Set[String],
                 relationships: Map[String, EntityId])
  extends Entity with Serializable {
  type EntityType = Query
}

object Query {
  type QueryId = String

  def apply(title: String,
            description: String,
            query: String,
            createdBy: Option[UserId],
            visibility: Visibility,
            tags: Set[String]): Query =
    apply(title, description, query, createdBy, Instant.now, createdBy, Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
}