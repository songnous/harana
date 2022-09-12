package com.harana.ui.external.lottie

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-lottie", JSImport.Default)
@js.native
object ReactLottie extends js.Object

@react object Lottie extends ExternalComponent {

  case class Props(ariaLabel: Option[String] = None,
                   ariaRole: Option[String] = None,
                   direction: Option[Double] = None,
                   eventListeners: List[EventListener] = List(),
                   height: Option[Double | String] = None,
                   isClickToPauseDisabled: Option[Boolean] = None,
                   isPaused: Option[Boolean] = None,
                   isStopped: Option[Boolean] = None,
                   options: Options = Options(),
                   segments: List[Double] = List(),
                   speed: Option[Double] = None,
                   title: Option[String] = None,
                   width: Option[Double | String] = None)

  override val component = ReactLottie
}

case class RendererSettings(className: Option[String],
                     clearCanvas: Option[Boolean],
                     context: Option[js.Any],
                     hideOnTransparent: Option[Boolean],
                     preserveAspectRatio: Option[Boolean],
                     progressiveLoad: Option[Boolean],
                     scaleMode: Option[js.Any])

case class EventListener(eventName: String, callback: () => Unit)

case class Options(animationData: Option[js.Any] = None,
                   autoplay: Option[Boolean] = None,
                   loop: Option[Boolean] = None,
                   rendererSettings: Option[RendererSettings] = None)