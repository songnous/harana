package com.harana.ui.external.simplemaps

import org.scalajs.dom.SVGPathElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-simple-maps", "Geography")
@js.native
object ReactGeography extends js.Object

@react object Geography extends ExternalComponent {

  case class Props(cacheId: Option[Double | String | Null] = None,
                   geography: Option[js.Object] = None,
                   onBlur: Option[(js.Object, FocusEvent[SVGPathElement]) => Unit] = None,
                   onClick: Option[(js.Object, MouseEvent[SVGPathElement, NativeMouseEvent]) => Unit] = None,
                   onFocus: Option[(js.Object, FocusEvent[SVGPathElement]) => Unit] = None,
                   onMouseDown: Option[(js.Object, MouseEvent[SVGPathElement, NativeMouseEvent]) => Unit] = None,
                   onMouseEnter: Option[(js.Object, MouseEvent[SVGPathElement, NativeMouseEvent]) => Unit] = None,
                   onMouseLeave: Option[(js.Object, MouseEvent[SVGPathElement, NativeMouseEvent]) => Unit] = None,
                   onMouseMove: Option[(js.Object, MouseEvent[SVGPathElement, NativeMouseEvent]) => Unit] = None,
                   onMouseUp: Option[(js.Object, MouseEvent[SVGPathElement, NativeMouseEvent]) => Unit] = None,
                   precision: Option[Double] = None,
                   projection: Option[js.Any] = None,
                   round: Option[Boolean] = None,
                   style: Option[Default] = None,
                   tabable: Option[Boolean] = None)

  override val component = ReactGeography
}