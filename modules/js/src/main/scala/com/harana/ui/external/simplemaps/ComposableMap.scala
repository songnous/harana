package com.harana.ui.external.simplemaps

import com.harana.ui.external.simplemaps.SimpleMaps.ProjectionFunction
import org.scalajs.dom.SVGDefsElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties
import typings.std

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-simple-maps", "ComposableMap")
@js.native
object ReactComposableMap extends js.Object

@react object ComposableMap extends ExternalComponent {

  case class Props(className: Option[String] = None,
                   defs: Option[SVGDefsElement] = None,
                   height: Option[Double] = None,
                   preserveAspectRatio: Option[String] = None,
                   projection: Option[String | ProjectionFunction] = None,
                   projectionConfig: Option[std.Partial[ProjectionConfig]] = None,
                   showCenter: Option[Boolean] = None,
                   style: Option[CSSProperties] = None,
                   viewBox: Option[String] = None,
                   width: Option[Double] = None)

  override val component = ReactComposableMap
}