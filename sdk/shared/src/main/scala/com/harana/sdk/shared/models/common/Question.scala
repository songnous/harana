package com.harana.sdk.shared.models.common

import java.time.Instant

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.Question.QuestionId
import com.harana.sdk.shared.models.common.User.UserId
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.utils.Random

@JsonCodec
case class Question(name: String,
                    category: String,
                    body: String,
                    votes: Int,
                    createdBy: Option[UserId],
                    created: Instant,
                    updatedBy: Option[UserId],
                    updated: Instant,
									  id: QuestionId,
                    status: Status,
										visibility: Visibility,
										version: Long,
										tags: Set[String],
                    relationships: Map[String, EntityId])
    extends Entity with Serializable {

	type EntityType = Question
}

object Question {
	type QuestionId = String

	def apply(name: String, category: String, body: String, votes: Int, createdBy: Option[User], visibility: Visibility, tags: Set[String]): Question = {
		apply(name, category, body, votes, createdBy.map(_.id), Instant.now, createdBy.map(_.id), Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
	}
}