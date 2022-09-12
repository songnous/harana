package com.harana.sdk.shared.models.query

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Entity, Status, Visibility}
import com.harana.sdk.shared.models.data.DataSource
import DataSet.DataSetId
import com.harana.sdk.shared.utils.Random
import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class DataSet(title: String,
									 description: String,
									 createdBy: Option[UserId],
									 created: Instant,
									 updatedBy: Option[UserId],
									 updated: Instant,
									 id: DataSetId,
									 status: Status,
									 visibility: Visibility,
									 version: Long,
									 tags: Set[String],
									 relationships: Map[String, EntityId])
	extends Entity with Serializable {

	type EntityType = DataSource
}

object DataSet {
	type DataSetId = String

	def apply(title: String, description: String, createdBy: Option[UserId], visibility: Visibility, tags: Set[String]): DataSet = {
		apply(title, description, createdBy, Instant.now, createdBy, Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
	}
}