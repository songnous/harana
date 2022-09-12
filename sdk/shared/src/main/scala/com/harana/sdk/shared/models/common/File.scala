package com.harana.sdk.shared.models.common

import java.time.Instant

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.File.FileId
import com.harana.sdk.shared.models.common.User.UserId
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.utils.Random

@JsonCodec
case class File(name: String,
                description: String,
                fileName: String,
                fileSize: Long,
                mimeType: String,
                createdBy: Option[UserId],
                created: Instant,
                updatedBy: Option[UserId],
                updated: Instant,
								id: FileId,
                status: Status,
								visibility: Visibility,
								version: Long,
							  tags: Set[String],
                relationships: Map[String, EntityId])
	extends Entity with Serializable {

	type EntityType = File
}

object File {
	type FileId = String

	def apply(name: String, description: String, fileName: String, fileSize: Long, mimeType: String, createdBy: Option[User], visibility: Visibility, tags: Set[String]): File = {
		apply(name, description, fileName, fileSize, mimeType, createdBy.map(_.id), Instant.now, createdBy.map(_.id), Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
	}
}