package com.harana.ui.external.circular_progress_bar

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-circular-progressbar", JSImport.Default)
@js.native
object ReactCircularProgressBar extends js.Object

@react object CircularProgressBar extends ExternalComponent {

  case class Props(background: Option[Boolean] = None,
                   backgroundPadding: Option[Double] = None,
                   className: Option[String] = None,
                   classes: Option[ProgressbarClasses] = None,
                   counterClockwise: Option[Boolean] = None,
                   initialAnimation: Option[Boolean] = None,
                   percentage: Double,
                   strokeWidth: Option[Double] = None,
                   styles: Option[ProgressbarStyles] = None,
                   text: Option[String] = None)

  override val component = ReactCircularProgressBar
}

case class ProgressbarClasses(background: Option[String] = None,
                            path: Option[String] = None,
                            root: Option[String] = None,
                            text: Option[String] = None,
                            trail: Option[String] = None)


case class ProgressbarStyles(background: Option[CSSProperties] = None,
                         path: Option[CSSProperties] = None,
                         root: Option[CSSProperties] = None,
                         text: Option[CSSProperties] = None,
                         trail: Option[CSSProperties] = None)