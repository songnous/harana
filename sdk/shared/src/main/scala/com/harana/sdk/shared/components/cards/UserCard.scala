package com.harana.sdk.shared.components.cards

import com.harana.sdk.shared.components.Orientation
import com.harana.sdk.shared.models.common.{Component, User}
import io.circe.generic.JsonCodec

@JsonCodec
case class UserCard(user: User,
                    orientation: Orientation = Orientation.Horizontal,
                    roundedThumbnail: Boolean = true,
                    showSocial: Boolean = true,
                    showMessaging: Boolean = true) extends Component