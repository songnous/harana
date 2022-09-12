package com.harana.sdk.shared.models.common

import java.time.Instant

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.Video.VideoId
import com.harana.sdk.shared.models.common.Workflow.WorkflowId
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.utils.Random

@JsonCodec
case class Workflow(name: String,
	                  createdBy: Option[UserId],
	                  created: Instant,
	                  updatedBy: Option[UserId],
	                  updated: Instant,
									  id: WorkflowId,
	                  status: Status,
										visibility: Visibility,
										version: Long,
										tags: Set[String],
										relationships: Map[String, EntityId])
	  extends Entity with Serializable {

	type EntityType = Workflow
}

object Workflow {
	type WorkflowId = String

	def apply(name: String, createdBy: Option[User], visibility: Visibility, tags: Set[String]): Workflow = {
		apply(name, createdBy.map(_.id), Instant.now, createdBy.map(_.id), Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
	}
}