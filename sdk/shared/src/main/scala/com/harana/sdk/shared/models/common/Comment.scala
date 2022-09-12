package com.harana.sdk.shared.models.common

import java.time.Instant

import com.harana.sdk.shared.models.common.Comment.CommentId
import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.User.UserId
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.utils.Random

@JsonCodec
case class Comment(comment: String,
                   votes: Int,
                   parent: Option[CommentId],
                   children: List[CommentId],
                   createdBy: Option[UserId],
                   created: Instant,
                   updatedBy: Option[UserId],
                   updated: Instant,
									 id: CommentId,
                   status: Status,
									 visibility: Visibility,
									 version: Long,
									 tags: Set[String],
                   relationships: Map[String, EntityId])
    extends Entity with Serializable {

	type EntityType = Comment
}

object Comment {
	type CommentId = String

	def apply(comment: String, votes: Int, createdBy: Option[User], visibility: Visibility, tags: Set[String]): Comment = {
		apply(comment, votes, None, List(), createdBy.map(_.id), Instant.now, createdBy.map(_.id), Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
	}

	def apply(comment: String, votes: Int, parent: Comment, children: List[Comment], createdBy: Option[User], visibility: Visibility, tags: Set[String]): Comment = {
		apply(comment, votes, Some(parent.id), children.map(_.id), createdBy.map(_.id), Instant.now, createdBy.map(_.id), Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
	}
}