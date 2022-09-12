package com.harana.sdk.shared.models.common

import java.time.Instant

import com.harana.sdk.shared.models.common.AuthenticationHandler.AuthenticationHandlerId
import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.User.UserId
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.utils.Random

@JsonCodec
case class AuthenticationHandler(name: String,
                                 description: String,
                                 parameterValues: Map[ParameterName, ParameterValue],
                                 createdBy: Option[UserId],
                                 created: Instant,
                                 updatedBy: Option[UserId],
                                 updated: Instant,
																 id: AuthenticationHandlerId,
                                 status: Status,
																 visibility: Visibility,
																 version: Long,
																 tags: Set[String],
                                 relationships: Map[String, EntityId])
    extends Entity with Serializable {

	type EntityType = AuthenticationHandler
}

object AuthenticationHandler {
	type AuthenticationHandlerId = String

	def apply(name: String, description: String, parameterValues: Map[ParameterName, ParameterValue], createdBy: Option[User], visibility: Visibility, tags: Set[String]): AuthenticationHandler = {
		apply(name, description, parameterValues, createdBy.map(_.id), Instant.now, createdBy.map(_.id), Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
	}
}