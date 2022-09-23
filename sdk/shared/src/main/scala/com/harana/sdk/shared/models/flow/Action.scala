package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.flow.Action.ActionId
import com.harana.sdk.shared.utils.Random
import io.circe.{Decoder, Encoder}
import io.circe.generic.JsonCodec

@JsonCodec
case class Action(id: ActionId,
                  actionType: ActionTypeInfo,
                  position: (Int, Int),
                  title: Option[String],
                  description: Option[String],
                  overrideColor: Option[String])

object Action {
	type ActionId = String

	def apply(actionType: ActionTypeInfo,
            position: (Int, Int),
            title: Option[String],
            description: Option[String],
            overrideColor: Option[String]): Action =
		apply(Random.long, actionType, position, title, description, overrideColor)


	implicit val decodeUri: Decoder[ActionTypeInfo] = Decoder.decodeString.emap { str => Left("") }
	implicit val encodeUri: Encoder[ActionTypeInfo] = Encoder.encodeString.contramap[ActionTypeInfo](_.toString)

}