package com.harana.sdk.shared.models.common

import java.time.Instant

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.Image.ImageId
import com.harana.sdk.shared.models.common.User.UserId
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.utils.Random

@JsonCodec
case class Image(name: String,
                 description: String,
                 fileName: String,
                 fileSize: Long,
                 width: Int,
                 height: Int,
                 mimeType: String,
                 createdBy: Option[UserId],
                 created: Instant,
                 updatedBy: Option[UserId],
                 updated: Instant,
								 id: ImageId,
                 status: Status,
								 visibility: Visibility,
								 version: Long,
								 tags: Set[String],
                 relationships: Map[String, EntityId])
    extends Entity with Serializable {

	type EntityType = Image
}

object Image {
	type ImageId = String

	def apply(name: String, description: String, fileName: String, fileSize: Long, width: Int, height: Int, mimeType: String, createdBy: Option[User], visibility: Visibility, tags: Set[String]): Image = {
		apply(name, description, fileName, fileSize, width, height, mimeType, createdBy.map(_.id), Instant.now, createdBy.map(_.id), Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
	}
}
