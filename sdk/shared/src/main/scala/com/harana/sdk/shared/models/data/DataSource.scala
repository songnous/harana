package com.harana.sdk.shared.models.data

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Background, Entity, Status, Visibility}
import com.harana.sdk.shared.models.data.DataSource.DataSourceId
import com.harana.sdk.shared.models.data.DataSourceType.DataSourceTypeId
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.utils.{HMap, Random}
import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class DataSource(title: String,
											description: String,
											dataSourceType: DataSourceTypeId,
											parameterValues: HMap[Parameter.Values],
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

	def apply(title: String, description: String, parameterValues: HMap[Parameter.Values], dataSourceType: DataSourceTypeId, path: Option[String], createdBy: Option[UserId], visibility: Visibility, background: Option[Background], tags: Set[String]): DataSource = {
		apply(title, description, dataSourceType, parameterValues, path, createdBy, Instant.now, createdBy, Instant.now, None, Random.long, Status.Active, visibility, 1L, background, tags, Map())
	}
}