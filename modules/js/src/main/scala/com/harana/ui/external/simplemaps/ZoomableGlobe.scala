package com.harana.ui.external.simplemaps

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-simple-maps", "ZoomableGlobe")
@js.native
object ReactZoomableGlobe extends js.Object

@react object ZoomableGlobe extends ExternalComponent {

  case class Props(center: Option[Point] = None,
                   disablePanning: Option[Boolean] = None,
                   height: Option[Double] = None,
                   onMoveEnd: Option[Point => Unit] = None,
                   onMoveStart: Option[Point => Unit] = None,
                   sensitivity: Option[Double] = None,
                   style: Option[CSSProperties] = None,
                   width: Option[Double] = None,
                   zoom: Option[Double] = None)

  override val component = ReactZoomableGlobe
}