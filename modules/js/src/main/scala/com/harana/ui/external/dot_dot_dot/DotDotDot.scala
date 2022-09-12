package com.harana.ui.external.dot_dot_dot

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-dotdotdot", JSImport.Default)
@js.native
object ReactDotDotDot extends js.Object

@react object DotDotDot extends ExternalComponent {

  case class Props(animate: Option[Boolean] = None,
                   clamp: Double | String | Boolean,
                   splitOnChars: List[String] = List(),
                   tagName: Option[String] = None,
                   truncationChar: Option[String] = None,
                   truncationHTML: Option[String] = None,
                   useNativeClamp: Option[Boolean] = None)

  override val component = ReactDotDotDot
}