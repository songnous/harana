package com.harana.ui.external.ion_slider

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSImport, JSName}
import scala.scalajs.js.|

@JSImport("react-ion-slider", JSImport.Default)
@js.native
object ReactIonSlider extends js.Object

@react object IonSlider extends ExternalComponent {

  case class Props(`type`: Option[String] = None,
                   min: Option[Double | Int] = None,
                   max: Option[Double | Int] = None,
                   from: Option[Double | Int] = None,
                   to: Option[Double | Int] = None,
                   step: Option[Double | Int] = None,
                   values: List[js.Any] = List(),
                   keyboard: Option[Boolean] = None,
                   grid: Option[Boolean] = None,
                   grid_margin: Option[Boolean] = None,
                   grid_num: Option[Int] = None,
                   grid_snap: Option[Boolean] = None,
                   drag_interval: Option[Boolean] = None,
                   min_interval: Option[Int] = None,
                   max_interval: Option[Int] = None,
                   from_fixed: Option[Boolean] = None,
                   from_min: Option[Int] = None,
                   from_max: Option[Int] = None,
                   from_shadow: Option[Boolean] = None,
                   to_fixed: Option[Boolean] = None,
                   to_min: Option[Int] = None,
                   to_max: Option[Int] = None,
                   to_shadow: Option[Boolean] = None,
                   skin: Option[String] = None,
                   hide_min_max: Option[Boolean] = None,
                   hide_from_to: Option[Boolean] = None,
                   force_edges: Option[Boolean] = None,
                   extra_classes: Option[String] = None,
                   block: Option[Boolean] = None,
                   prettify_enabled: Option[Boolean] = None,
                   prettify_separator: Option[String] = None,
                   prettify: Option[Boolean] = None,
                   prefix: Option[String] = None,
                   postfix: Option[String] = None,
                   max_postfix: Option[String] = None,
                   decorate_both: Option[Boolean] = None,
                   value_separator: Option[String] = None,
                   input_values_separator: Option[String] = None,
                   disable: Option[Boolean] = None,
                   scope: Option[js.Object] = None,
                   onStart: Option[SliderData => Unit] = None,
                   onChange: Option[SliderData => Unit] = None,
                   onFinish: Option[SliderData => Unit] = None,
                   onUpdate: Option[SliderData => Unit] = None)

                   override val component = ReactIonSlider
}

case class SliderData(input: js.Object,
                      slider: js.Object,
                      min: Int,
                      max: Int,
                      from: Int,
                      from_percent: Int,
                      from_value: Int,
                      to: Int,
                      to_percent: Int,
                      to_value: Int,
                      min_pretty: String,
                      max_pretty: String,
                      fromPretty: String,
                      toPretty: String)