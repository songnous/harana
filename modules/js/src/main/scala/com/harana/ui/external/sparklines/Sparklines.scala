package com.harana.ui.external.sparklines

import com.harana.ui.external.sparklines.Types._
import org.scalajs.dom.{Event, HTMLElement}
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("@data-ui/sparkline", "BandLine")
@js.native
object ReactBandLine extends js.Object

@react object BandLine extends ExternalComponent {

  case class Props(band: Option[String | Band] = None,
                   fill: Option[String] = None,
                   fillOpacity: Option[Double] = None,
                   stroke: Option[String] = None,
                   strokeWidth: Option[Int] = None)

  override val component = ReactBandLine
}

@JSImport("@data-ui/sparkline", "BarSeries")
@js.native
object ReactBarSeries extends js.Object

@react object BarSeries extends ExternalComponent {

  case class Props(fill: Option[String | Fill] = None,
                   fillOpacity: Option[Double | FillOpacity] = None,
                   LabelComponent: Option[HTMLElement] = None,
                   labelOffset: Option[Int] = None,
                   labelPosition: Option[String | LabelPosition] = None,
                   onMouseMove: Option[OnMouseMove] = None,
                   onMouseLeave: Option[OnMouseLeave] = None,
                   renderLabel: Option[RenderLabel] = None,
                   stroke: Option[String | Stroke] = None,
                   strokeWidth: Option[Int | StrokeWidth] = None)

  override val component = ReactBarSeries
}

@JSImport("@data-ui/sparkline", "HorizontalReferenceLine")
@js.native
object ReactHorizontalReferenceLine extends js.Object

@react object HorizontalReferenceLine extends ExternalComponent {

  case class Props(reference: Option[Int | String] = None,
                   LabelComponent: Option[HTMLElement] = None,
                   labelOffset: Option[Int] = None,
                   labelPosition: Option[String] = None,
                   renderLabel: Option[RenderLabel] = None,
                   stroke: Option[String] = None,
                   strokeDasharray: Option[String] = None,
                   strokeLinecap: Option[String] = None,
                   strokeWidth: Option[Int] = None)

  override val component = ReactHorizontalReferenceLine
}

@JSImport("@data-ui/sparkline", "LineSeries")
@js.native
object ReactLineSeries extends js.Object

@react object LineSeries extends ExternalComponent {

  case class Props(fill: Option[String | Fill] = None,
                   fillOpacity: Option[Double | FillOpacity] = None,
                   curve: Option[String] = None,
                   onMouseMove: Option[OnMouseMove] = None,
                   onMouseLeave: Option[OnMouseLeave] = None,
                   showArea: Option[Boolean] = None,
                   showLine: Option[Boolean] = None,
                   stroke: Option[String] = None,
                   strokeDasharray: Option[String] = None,
                   strokeLinecap: Option[String] = None,
                   strokeWidth: Option[Int] = None)

  override val component = ReactLineSeries
}

@JSImport("@data-ui/sparkline", "LinearGradient")
@js.native
object ReactLinearGradient extends js.Object

@react object LinearGradient extends ExternalComponent {

  case class Props(id: String,
                   from: Option[String] = None,
                   to: Option[String] = None,
                   fromOffset: Option[String] = None,
                   fromOpacity: Option[Int] = None,
                   toOffset: Option[String] = None,
                   toOpacity: Option[Int] = None,
                   rotate: Option[String | Int] = None,
                   transform: Option[String] = None)

  override val component = ReactLinearGradient
}

@JSImport("@data-ui/sparkline", "PatternLines")
@js.native
object ReactPatternLines extends js.Object

@react object PatternLines extends ExternalComponent {

  case class Props(id: String,
                   width: Int,
                   height: Int,
                   stroke: String,
                   strokeWidth: Int,
                   strokeDasharray: Option[String] = None,
                   strokeLinecap: Option[String] = None,
                   shapeRendering: Option[String] = None,
                   orientation: Option[String] = None,
                   background: Option[String] = None,
                   className: Option[String] = None)

  override val component = ReactPatternLines
}

@JSImport("@data-ui/sparkline", "PointSeries")
@js.native
object ReactPointSeries extends js.Object

@react object PointSeries extends ExternalComponent {

  case class Props(fill: Option[String | Fill] = None,
                   fillOpacity: Option[Double | FillOpacity] = None,
                   LabelComponent: Option[HTMLElement] = None,
                   labelOffset: Option[Int] = None,
                   labelPosition: Option[String | LabelPosition] = None,
                   onMouseMove: Option[OnMouseMove] = None,
                   onMouseLeave: Option[OnMouseLeave] = None,
                   points: List[Int | String] = List(),
                   size: Option[Int | Size] = None,
                   renderLabel: Option[RenderLabel] = None,
                   stroke: Option[String | Stroke] = None,
                   strokeWidth: Option[Int | StrokeWidth] = None)

  override val component = ReactPointSeries
}

@JSImport("@data-ui/sparkline", "Sparkline")
@js.native
object ReactSparkline extends js.Object

@react object Sparkline extends ExternalComponent {

  case class Props(ariaLabel: String,
                   className: Option[String] = None,
                   data: List[Double] = List(),
                   height: Int,
                   margin: Option[Margin] = None,
                   max: Option[Double] = None,
                   min: Option[Double] = None,
                   onMouseMove: Option[OnMouseMove] = None,
                   onMouseLeave: Option[OnMouseLeave] = None,
                   styles: Option[js.Object] = None,
                   width: Int,
                   valueAccessor: Option[ValueAccessor] = None)

  override val component = ReactSparkline
}

@JSImport("@data-ui/sparkline", "VerticalReferenceLine")
@js.native
object ReactVerticalReferenceLine extends js.Object

@react object VerticalReferenceLine extends ExternalComponent {

  case class Props(reference: Option[Int | String] = None,
                   LabelComponent: Option[HTMLElement] = None,
                   labelOffset: Option[Int] = None,
                   labelPosition: Option[String] = None,
                   renderLabel: Option[RenderLabel] = None,
                   stroke: Option[String] = None,
                   strokeDasharray: Option[String] = None,
                   strokeLinecap: Option[String] = None,
                   strokeWidth: Option[Int] = None)

  override val component = ReactVerticalReferenceLine
}

case class Band(x: Int, y: Int)
case class Margin(top: Number, right: Number, bottom: Number, left: Number)

object Types {
  type Fill = (Double, Int) => String
  type FillOpacity = (Double, Int) => Double
  type LabelPosition = (Double, Int) => String
  type Number = Int | Double
  type OnMouseMove = (Event, List[Double], Double, String) => () => Unit
  type OnMouseLeave = (Event, List[Double], Double, String) => () => Unit
  type RenderLabel = Double => String
  type Size = (Double, Int) => Int
  type Stroke = (Double, Int) => String
  type StrokeWidth = (Double, Int) => Int
  type ValueAccessor = Double => Double
}