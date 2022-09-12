package com.harana.sdk.shared.components.lists

import com.harana.sdk.shared.models.common.{Component, User}
import io.circe.generic.JsonCodec

@JsonCodec
case class UsersList(title: String,
                     icon: Option[String] = None,
                     users: List[User] = List.empty,
                     showPosition: Boolean = false,
                     showOnline: Boolean = false,
                     showMessenging: Boolean = false) extends Component
