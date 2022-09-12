package com.harana.ui.external.slider

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-slider", JSImport.Default)
@js.native
object ReactSlider extends js.Object

@react object Slider extends ExternalComponent {

  case class Props(barClassName: Option[String] = None,
                   className: Option[String] = None,
                   defaultValue: Option[Double | List[Double]] = None,
                   disabled: Option[Boolean] = None,
                   handleActiveClassName: Option[String] = None,
                   handleClassName: Option[String] = None,
                   invert: Option[Boolean] = None,
                   max: Option[Double] = None,
                   min: Option[Double] = None,
                   minDistance: Option[Double] = None,
                   onAfterChange: Option[Double | List[Double] | Null => Unit] = None,
                   onBeforeChange: Option[Double | List[Double] | Null => Unit] = None,
                   onChange: Option[Double | List[Double] | Null => Unit] = None,
                   onSliderClick: Option[Double => Unit] = None,
                   orientation: Option[String] = None,
                   pearling: Option[Boolean] = None,
                   snapDragDisabled: Option[Boolean] = None,
                   step: Option[Double] = None,
                   value: Option[Double | List[Double]] = None,
                   withBars: Option[Boolean] = None)

  override val component = ReactSlider
}