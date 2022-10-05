package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.flow.Action.ActionId
import com.harana.sdk.shared.models.flow.graph.GraphAction
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.utils.{HMap, Random}
import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.generic.JsonCodec

@JsonCodec
case class Action[T <: ActionTypeInfo](id: ActionId,
                                       position: (Int, Int),
                                       inArity: Int,
                                       outArity: Int,
                                       title: Option[String],
                                       description: Option[String],
                                       overrideColor: Option[String],
                                       parameterValues: HMap[Parameter.Values]) extends GraphAction

object Action {
  type ActionId = String

  def apply[T <: ActionTypeInfo](position: (Int, Int),
                                 inArity: Int,
                                 outArity: Int,
                                 title: Option[String],
                                 description: Option[String],
                                 overrideColor: Option[String],
                                 parameterValues: HMap[Parameter.Values]): Action[T] =
    Action(Random.long, position, inArity, outArity, title, description, overrideColor, parameterValues)

  implicit val encoder: Encoder[Action[_ <: ActionTypeInfo]] = Encoder.instance[Action[_ <: ActionTypeInfo]] { action =>
    Json.obj()
  }

  implicit val decoder: Decoder[Action[_ <: ActionTypeInfo]] = (c: HCursor) =>
    null

  implicit val nodeEncoder: Encoder[Node[Action[_ <: ActionTypeInfo]]] = Encoder.instance[Node[Action[_ <: ActionTypeInfo]]] { action =>
    Json.obj()
  }

  implicit val nodeDecoder: Decoder[Node[Action[_ <: ActionTypeInfo]]] = (c: HCursor) =>
    null
}
