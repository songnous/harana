package com.harana.ui.external.measure

import com.harana.ui.external.measure.Types._
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.SFC

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-measure", JSImport.Default)
@js.native
object ReactMeasure extends js.Object

@react object Measure extends ExternalComponent {

  case class Props(bounds: Option[Boolean] = None,
                   children: Option[SFC[MeasuredComponentProps]] = None,
                   client: Option[Boolean] = None,
                   innerRef: Option[Element => Unit | Null => Unit] = None,
                   margin: Option[Boolean] = None,
                   offset: Option[Boolean] = None,
                   onResize: Option[ContentRect => Unit] = None,
                   scroll: Option[Boolean] = None)

  override val component = ReactMeasure
}

object Types {
  type BoundingRect = Dimensions with Margin
  type Margin = TopLeft with BottomRight
  type MeasurementType = String
  type Rect = TopLeft with Dimensions
}

@js.native
trait BottomRight extends js.Object {
  val bottom: Double = js.native
  val right: Double = js.native
}

@js.native
trait ContentRect extends js.Object {
  val bounds: Option[BoundingRect] = js.native
  val client: Option[Rect] = js.native
  val entry: Option[js.Any] = js.native
  val margin: Option[Margin] = js.native
  val offset: Option[Rect] = js.native
  val scroll: Option[Rect] = js.native
}

@js.native
trait Dimensions extends js.Object {
  val height: Double = js.native
  val width: Double = js.native
}

@js.native
trait MeasuredComponentProps extends js.Object {
  val contentRect: ContentRect = js.native
  val measure: js.Object = js.native
  val measureRef: js.Object = js.native
}

@js.native
trait TopLeft extends js.Object {
  val left: Double = js.native
  val top: Double = js.native
}