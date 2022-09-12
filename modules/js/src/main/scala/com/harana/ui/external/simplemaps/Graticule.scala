package com.harana.ui.external.simplemaps

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-simple-maps", "Graticule")
@js.native
object ReactGraticule extends js.Object

@react object Graticule extends ExternalComponent {

  case class Props(Globe: Option[Boolean] = None,
                   disableOptimization: Option[Boolean] = None,
                   fill: Option[String] = None,
                   outline: Option[Boolean] = None,
                   precision: Option[Double] = None,
                   round: Option[Boolean] = None,
                   step: Option[Point] = None,
                   stroke: Option[String] = None,
                   style: Option[CSSProperties] = None)

  override val component = ReactGraticule
}