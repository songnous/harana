package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.flow.Action.ActionId
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.utils.{HMap, Random}
import io.circe.generic.JsonCodec

@JsonCodec
case class Action[T <: ActionTypeInfo](id: ActionId,
                                       position: (Int, Int),
                                       title: Option[String],
                                       description: Option[String],
                                       overrideColor: Option[String],
                                       parameterValues: HMap[Parameter.Values])

object Action {
  type ActionId = String

  def apply[T <: ActionTypeInfo](position: (Int, Int),
                                 title: Option[String],
                                 description: Option[String],
                                 overrideColor: Option[String],
                                 parameterValues: HMap[Parameter.Values]): Action[T] =
    Action(Random.long, position, title, description, overrideColor, parameterValues)
}