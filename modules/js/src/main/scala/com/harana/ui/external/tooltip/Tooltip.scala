package com.harana.ui.external.tooltip

import org.scalajs.dom.Event
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-tooltip", JSImport.Default)
@js.native
object ReactTooltip extends js.Object

@react object Tooltip extends ExternalComponent {

  case class Props(place: Option[String] = None,
                  `type`: Option[String] = None,
                   effect: Option[String] = None,
                   event: Option[String] = None,
                   eventOff: Option[String] = None,
                   globalEventOff: Option[String] = None,
                   isCapture: Option[Boolean] = None,
                   offset: Option[Offset] = None,
                   multiline: Option[Boolean] = None,
                   className: Option[String] = None,
                   html: Option[Boolean] = None,
                   delayHide: Option[Int] = None,
                   delayShow: Option[Int] = None,
                   delayUpdate: Option[Int] = None,
                   insecure: Option[Boolean] = None,
                   border: Option[Boolean] = None,
                   getContent: Option[String => Unit] = None,
                   afterShow: Option[Event => Unit] = None,
                   afterHide: Option[Event => Unit] = None,
                   disable: Option[Boolean] = None,
                   scrollHide: Option[Boolean] = None,
                   resizeHide: Option[Boolean] = None,
                   wrapper: Option[String] = None)

  override val component = ReactTooltip
}

case class Offset(top: Int, left: Int)