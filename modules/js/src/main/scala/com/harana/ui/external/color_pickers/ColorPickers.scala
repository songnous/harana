package com.harana.ui.external.color_pickers

import com.harana.ui.external.color_pickers.Types._
import org.scalajs.dom.Event
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-color", "BlockPicker")
@js.native
object ReactBlockPicker extends js.Object

@react object BlockPicker extends ExternalComponent {

  case class Props(color: Option[String | RGB | HSL] = None,
                   width: Option[String | Int] = None,
                   colors: List[String] = List(),
                   triangle: Option[String] = None,
                   onChange: Option[OnChange] = None,
                   onChangeComplete: Option[OnChangeComplete] = None,
                   onSwatchHover: Option[OnSwatchHover] = None)

  override val component = ReactBlockPicker
}

@JSImport("react-color", "ChromePicker")
@js.native
object ReactChromePicker extends js.Object

@react object ChromePicker extends ExternalComponent {

case class Props(color: Option[String | RGB | HSL] = None,
                 disableAlpha: Option[Boolean] = None,
                 renderers: Option[js.Object] = None,
                 onChange: Option[OnChange] = None,
                 onChangeComplete: Option[OnChangeComplete] = None)

  override val component = ReactChromePicker
}

@JSImport("react-color", "CirclePicker")
@js.native
object ReactCirclePicker extends js.Object

@react object CirclePicker extends ExternalComponent {

  case class Props(color: Option[String | RGB | HSL] = None,
                   width: Option[String | Int] = None,
                   colors: List[String] = List(),
                   circleSize: Option[Int] = None,
                   circleSpacing: Option[Int] = None,
                   onChange: Option[OnChange] = None,
                   onChangeComplete: Option[OnChangeComplete] = None,
                   onSwatchHover: Option[OnSwatchHover] = None)

  override val component = ReactCirclePicker
}

@JSImport("react-color", "CompactPicker")
@js.native
object ReactCompactPicker extends js.Object

@react object CompactPicker extends ExternalComponent {

  case class Props(color: Option[String | RGB | HSL] = None,
                   disableAlpha: Option[Boolean] = None,
                   renderers: Option[js.Object] = None,
                   onChange: Option[OnChange] = None,
                   onChangeComplete: Option[OnChangeComplete] = None)

  override val component = ReactCompactPicker
}

@JSImport("react-color", "GithubPicker")
@js.native
object ReactGithubPicker extends js.Object

@react object GithubPicker extends ExternalComponent {

  case class Props(color: Option[String | RGB | HSL] = None,
                   width: Option[String | Int] = None,
                   colors: List[String] = List(),
                   triangle: Option[String] = None,
                   onChange: Option[OnChange] = None,
                   onChangeComplete: Option[OnChangeComplete] = None,
                   onSwatchHover: Option[OnSwatchHover] = None)

  override val component = ReactGithubPicker
}

@JSImport("react-color", "HuePicker")
@js.native
object ReactHuePicker extends js.Object

@react object HuePicker extends ExternalComponent {

  case class Props(color: Option[String | RGB | HSL] = None,
                   width: Option[String | Int] = None,
                   height: Option[String | Int] = None,
                   direction: Option[String] = None,
                   pointer: Option[js.Any] = None,
                   onChange: Option[OnChange] = None,
                   onChangeComplete: Option[OnChangeComplete] = None,
                   onSwatchHover: Option[OnSwatchHover] = None)

  override val component = ReactHuePicker
}

@JSImport("react-color", "PhotoshopPicker")
@js.native
object ReactPhotoshopPicker extends js.Object

@react object PhotoshopPicker extends ExternalComponent {

  case class Props(color: Option[String | RGB | HSL] = None,
                   header: Option[String] = None,
                   height: Option[String | Int] = None,
                   direction: Option[String] = None,
                   pointer: Option[js.Any] = None,
                   onChange: Option[OnChange] = None,
                   onChangeComplete: Option[OnChangeComplete] = None,
                   onAccept: Option[() => Unit] = None,
                   onCancel: Option[() => Unit] = None)

  override val component = ReactPhotoshopPicker
}

@JSImport("react-color", "SketchPicker")
@js.native
object ReactSketchPicker extends js.Object

@react object SketchPicker extends ExternalComponent {

  case class Props(color: Option[String | RGB | HSL] = None,
                   disableAlpha: Option[Boolean] = None,
                   presetColors: List[SketchColor] = List(),
                   width: Option[String | Int] = None,
                   renderers: Option[js.Object] = None,
                   onChange: Option[OnChange] = None,
                   onChangeComplete: Option[OnChangeComplete] = None,
                   onSwatchHover: Option[OnSwatchHover] = None)

  override val component = ReactSketchPicker
}

@JSImport("react-color", "SliderPicker")
@js.native
object ReactSliderPicker extends js.Object

@react object SliderPicker extends ExternalComponent {

  case class Props(color: Option[String | RGB | HSL] = None,
                   pointer: Option[js.Any] = None,
                   onChange: Option[OnChange] = None,
                   onChangeComplete: Option[OnChangeComplete] = None)

  override val component = ReactSliderPicker
}

@JSImport("react-color", "SwatchesPicker")
@js.native
object ReactSwatchesPicker extends js.Object

@react object SwatchesPicker extends ExternalComponent {

  case class Props(color: Option[String | RGB | HSL] = None,
                   width: Option[String | Int] = None,
                   height: Option[String | Int] = None,
                   colors: List[js.Array[String]] = List(),
                   onChange: Option[OnChange] = None,
                   onChangeComplete: Option[OnChangeComplete] = None,
                   onSwatchHover: Option[OnSwatchHover] = None)

  override val component = ReactSwatchesPicker
}

@JSImport("react-color", "TwitterPicker")
@js.native
object ReactTwitterPicker extends js.Object

@react object TwitterPicker extends ExternalComponent {

  case class Props(color: Option[String | RGB | HSL] = None,
                   width: Option[String | Int] = None,
                   height: Option[String | Int] = None,
                   colors: List[String] = List(),
                   onChange: Option[OnChange] = None,
                   onChangeComplete: Option[OnChangeComplete] = None,
                   onSwatchHover: Option[OnSwatchHover] = None)

  override val component = ReactTwitterPicker
}

object Types {
  type OnChange = Color => Unit
  type OnChangeComplete = Color => Unit
  type OnSwatchHover = Color => Unit
}

@js.native
trait Color extends js.Object {
  val hex: String = js.native
  val rgb: ColorRGB = js.native
  val hsl: ColorHSL = js.native
}

@js.native
trait ColorRGB extends js.Object {
  val r: Int = js.native
  val g: Int = js.native
  val b: Int = js.native
  val a: Float = js.native
}

@js.native
trait ColorHSL extends js.Object {
  val h: Float = js.native
  val s: Float = js.native
  val l: Float = js.native
}

@js.native
trait RGB extends js.Object {
  val r: Int = js.native
  val g: Int = js.native
  val b: Int = js.native
  val a: Option[Float] = js.native
}

@js.native
trait HSL extends js.Object {
  val h: Float = js.native
  val s: Float = js.native
  val l: Float = js.native
  val a: Option[Float] = js.native
}

@js.native
trait SketchColor extends js.Object {
  val color: String = js.native
  val title: String = js.native
}