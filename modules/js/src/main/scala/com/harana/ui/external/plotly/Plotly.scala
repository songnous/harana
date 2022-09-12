package com.harana.ui.external.plotly

import org.scalajs.dom.HTMLElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties
import typings.std

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-plotly.js", JSImport.Default)
@js.native
object ReactPlotly extends js.Object

@react object Plotly extends ExternalComponent {

  case class Props(className: Option[String] = None,
                   config: Option[js.Object] = None,
                   data: List[Unit] = List(),
                   debug: Option[Boolean] = None,
                   divId: Option[String] = None,
                   frames: List[Frame] = List(),
                   layout: Option[std.Partial[Unit]] = None,
                   onAfterExport: Option[() => Unit] = None,
                   onAfterPlot: Option[() => Unit] = None,
                   onAnimated: Option[() => Unit] = None,
                   onAnimatingFrame: Option[() => Unit] = None,
                   onAnimationInterrupted: Option[() => Unit] = None,
                   onAutoSize: Option[() => Unit] = None,
                   onBeforeExport: Option[() => Unit] = None,
                   onButtonClicked: Option[() => Unit] = None,
                   onClick: Option[() => Unit] = None,
                   onClickAnnotation: Option[() => Unit] = None,
                   onDeselect: Option[() => Unit] = None,
                   onDoubleClick: Option[() => Unit] = None,
                   onError: Option[js.Object => Unit] = None,
                   onFramework: Option[() => Unit] = None,
                   onHover: Option[() => Unit] = None,
                   onInitialized: Option[(Figure, HTMLElement) => Unit] = None,
                   onLegendClick: Option[() => Boolean] = None,
                   onLegendDoubleClick: Option[() => Boolean] = None,
                   onPurge: Option[(Figure, HTMLElement) => Unit] = None,
                   onRedraw: Option[() => Unit] = None,
                   onRelayout: Option[() => Unit] = None,
                   onRestyle: Option[() => Unit] = None,
                   onSelected: Option[() => Unit] = None,
                   onSelecting: Option[() => Unit] = None,
                   onSliderChange: Option[() => Unit] = None,
                   onSliderEnd: Option[() => Unit] = None,
                   onSliderStart: Option[() => Unit] = None,
                   onTransitionInterrupted: Option[() => Unit] = None,
                   onTransitioning: Option[() => Unit] = None,
                   onUnhover: Option[() => Unit] = None,
                   onUpdate: Option[(Figure, HTMLElement) => Unit] = None,
                   revision: Option[Double] = None,
                   style: Option[CSSProperties] = None,
                   useResizeHandler: Option[Boolean] = None)

  override val component = ReactPlotly
}

@js.native
trait Figure extends js.Object {
  val data: List[js.Object] = js.native
  val layout: js.Object = js.native
}

@js.native
trait Frame extends js.Object {
  val data: List[Point] = js.native
  val group: String = js.native
  val name: String = js.native
}

@js.native
trait Point extends js.Object {
  val x: Int = js.native
  val y: Int = js.native
}