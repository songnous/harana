package com.harana.ui.external.simplemaps

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.{FocusEvent, MouseEvent, NativeMouseEvent}
import typings.std.SVGGElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-simple-maps", "Marker")
@js.native
object ReactMarker extends js.Object

@react object Marker extends ExternalComponent {

  case class Props(marker: Option[MarkerType] = None,
                   onBlur: Option[(MarkerType, FocusEvent[SVGGElement]) => Unit] = None,
                   onClick: Option[(MarkerType, MouseEvent[SVGGElement, NativeMouseEvent]) => Unit] = None,
                   onFocus: Option[(MarkerType, FocusEvent[SVGGElement]) => Unit] = None,
                   onMouseDown: Option[(MarkerType, MouseEvent[SVGGElement, NativeMouseEvent]) => Unit] = None,
                   onMouseEnter: Option[(MarkerType, MouseEvent[SVGGElement, NativeMouseEvent]) => Unit] = None,
                   onMouseLeave: Option[(MarkerType, MouseEvent[SVGGElement, NativeMouseEvent]) => Unit] = None,
                   onMouseMove: Option[(MarkerType, MouseEvent[SVGGElement, NativeMouseEvent]) => Unit] = None,
                   onMouseUp: Option[(MarkerType, MouseEvent[SVGGElement, NativeMouseEvent]) => Unit] = None,
                   preserveMarkerAspect: Option[Boolean] = None,
                   style: Option[Default] = None,
                   tabable: Option[Boolean] = None)

  override val component = ReactMarker
}