package com.harana.ui.external.simplemaps

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-simple-maps", "ZoomableGroup")
@js.native
object ReactZoomableGroup extends js.Object

@react object ZoomableGroup extends ExternalComponent {

  case class Props(backdrop: Option[Point] = None,
                   center: Option[Point] = None,
                   disablePanning: Option[Boolean] = None,
                   height: Option[Double] = None,
                   onMoveEnd: Option[Point => Unit] = None,
                   onMoveStart: Option[Point => Unit] = None,
                   style: Option[CSSProperties] = None,
                   width: Option[Double] = None,
                   zoom: Option[Double] = None)

  override val component = ReactZoomableGroup
}