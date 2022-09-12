package com.harana.sdk.shared.models.catalog

import java.time.Instant
import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.catalog.PageExporter.PageExporterId
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Entity, ParameterValue, Status, User, Visibility}
import com.harana.sdk.shared.models.common.{ParameterValue, Visibility}
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.utils.Random
import io.circe.generic.JsonCodec

@JsonCodec
case class PageExporter(name: String,
                        description: String,
                        parameterValues: Map[ParameterName, ParameterValue],
                        createdBy: Option[UserId],
                        created: Instant,
                        updatedBy: Option[UserId],
                        updated: Instant,
												id: PageExporterId,
                        status: Status,
												visibility: Visibility,
												version: Long,
											  tags: Set[String],
                        relationships: Map[String, EntityId])
    extends Entity with Serializable {

	type EntityType = PageExporter
}

object PageExporter {
	type PageExporterId = String

	def apply(name: String, description: String, parameterValues: Map[ParameterName, ParameterValue], createdBy: Option[User], visibility: Visibility, tags: Set[String]): PageExporter = {
		apply(name, description, parameterValues, createdBy.map(_.id), Instant.now, createdBy.map(_.id), Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
	}
}
