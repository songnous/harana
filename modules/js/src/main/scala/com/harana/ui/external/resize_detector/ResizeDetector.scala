package com.harana.ui.external.resize_detector

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-resize-detector", JSImport.Default)
@js.native
object ReactResizeDetector extends js.Object

@react object ResizeDetector extends ExternalComponent {

  case class Props(handleHeight: Option[Boolean] = None,
                   handleWidth: Option[Boolean] = None,
                   onResize: Option[(Double, Double) => Unit] = None,
                   refreshMode: Option[String],
                   refreshRate: Option[Double] = None,
                   render: Option[js.Any => ReactElement] = None,
                   resizableElementId: Option[String] = None,
                   skipOnMount: Option[Boolean] = None)

  override val component = ReactResizeDetector
}