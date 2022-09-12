package com.harana.ui.external.avatar

import org.scalajs.dom.MouseEvent
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-avatar", JSImport.Default)
@js.native
object ReactAvatar extends js.Object

@react object Avatar extends ExternalComponent {

  case class Props(className: Option[String] = None,
                   email: Option[String] = None,
                   md5Email: Option[String] = None,
                   facebookId: Option[String] = None,
                   twitterHandle: Option[String] = None,
                   instagramId: Option[String] = None,
                   googleId: Option[String] = None,
                   skypeId: Option[String] = None,
                   name: Option[String] = None,
                   maxInitials: Option[Int | Float] = None,
                   value: Option[String] = None,
                   color: Option[String] = None,
                   fgColor: Option[String] = None,
                   size: Option[Int] = None,
                   textSizeRatio: Option[Int | Float] = None,
                   round: Option[Boolean | String] = None,
                   src: Option[String] = None,
                   style: Option[js.Object] = None,
                   unstyled: Option[Boolean] = None,
                   onClick: Option[MouseEvent => Unit] = None)

  override val component = ReactAvatar
}