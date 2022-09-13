package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.flow.Action.ActionId
import com.harana.sdk.shared.utils.Random
import io.circe.generic.JsonCodec

@JsonCodec
case class Action(id: ActionId,
									action: ActionInfo.Id,
									position: (Int, Int),
									title: Option[String],
									description: Option[String],
									overrideColor: Option[String])

object Action {
	type ActionId = String

	def apply(action: ActionInfo.Id,
						position: (Int, Int),
						title: Option[String],
						description: Option[String],
						overrideColor: Option[String]): Action =
		apply(Random.long, action, position, title, description, overrideColor)
}