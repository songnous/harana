package com.harana.sdk.shared.models.data

import java.time.Instant
import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Background, Entity, ParameterValue, Status, User, Visibility}
import DataSource.DataSourceId
import DataSourceType.DataSourceTypeId
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, Parameters}
import com.harana.sdk.shared.utils.Random
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._
import io.circe.{Decoder, Encoder}

@JsonCodec
case class DataSource(title: String,
											description: String,
											dataSourceType: DataSourceTypeId,
											path: Option[String],
											createdBy: Option[UserId],
											created: Instant,
											updatedBy: Option[UserId],
											updated: Instant,
											lastSynced: Option[Instant],
											id: DataSourceId,
											status: Status,
											visibility: Visibility,
											version: Long,
											background: Option[Background],
											tags: Set[String],
											relationships: Map[String, EntityId])
    extends Entity with Serializable {

	type EntityType = DataSource
}

object DataSource {
	type DataSourceId = String

	def apply(title: String, description: String, dataSourceType: DataSourceTypeId, path: Option[String], createdBy: Option[UserId], visibility: Visibility, background: Option[Background], tags: Set[String]): DataSource = {
		apply(title, description, dataSourceType, path, createdBy, Instant.now, createdBy, Instant.now, None, Random.long, Status.Active, visibility, 1L, background, tags, Map())
	}
}