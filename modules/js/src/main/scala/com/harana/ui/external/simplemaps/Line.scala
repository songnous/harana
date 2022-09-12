package com.harana.ui.external.simplemaps

import org.scalajs.dom.SVGPathElement
import org.scalajs.dom.svg.Line
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-simple-maps", "Line")
@js.native
object ReactLine extends js.Object

@react object Line extends ExternalComponent {

  case class Props(buildPath: Option[(Point, Point, Line) => String] = None,
                   line: Option[Line] = None,
                   onBlur: Option[(Line, FocusEvent[SVGPathElement]) => Unit] = None,
                   onClick: Option[(Line, MouseEvent[SVGPathElement, NativeMouseEvent]) => Unit] = None,
                   onFocus: Option[(Line, FocusEvent[SVGPathElement]) => Unit] = None,
                   onMouseDown: Option[(Line, MouseEvent[SVGPathElement, NativeMouseEvent]) => Unit] = None,
                   onMouseEnter: Option[(Line, MouseEvent[SVGPathElement, NativeMouseEvent]) => Unit] = None,
                   onMouseLeave: Option[(Line, MouseEvent[SVGPathElement, NativeMouseEvent]) => Unit] = None,
                   onMouseMove: Option[(Line, MouseEvent[SVGPathElement, NativeMouseEvent]) => Unit] = None,
                   onMouseUp: Option[(Line, MouseEvent[SVGPathElement, NativeMouseEvent]) => Unit] = None,
                   preserveMarkerAspect: Option[Boolean] = None,
                   style: Option[Default] = None,
                   tabable: Option[Boolean] = None)

  override val component = ReactLine
}