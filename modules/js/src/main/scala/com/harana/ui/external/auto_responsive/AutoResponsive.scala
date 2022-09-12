package com.harana.ui.external.auto_responsive

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("autoresponsive-react", JSImport.Default)
@js.native
object ReactAutoResponsive extends js.Object

@react object AutoResponsive extends ExternalComponent {

  case class Props(containerWidth: Option[String | Int | Float] = None,
                   containerHeight: Option[String | Int | Float] = None,
                   gridWidth: Option[Int | Float] = None,
                   prefixClassName: Option[String] = None,
                   itemClassName: Option[String] = None,
                   itemMargin: Option[Int | Float] = None,
                   horizontalDirection: Option[String] = None,
                   verticalDirection: Option[String] = None,
                   transitionDuration: Option[Int | Float] = None,
                   transitionTimingFunction: Option[String] = None,
                   closeAnimation: Option[Boolean] = None,
                   onItemDidLayout: Option[js.Object => () => Unit] = None,
                   onContainerDidLayout: Option[() => Unit] = None)

  override val component = ReactAutoResponsive
}