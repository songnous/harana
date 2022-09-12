package com.harana.ui.external.simplemaps

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-simple-maps", "Annotation")
@js.native
object ReactAnnotation extends js.Object

@react object Annotation extends ExternalComponent {

  case class Props(curve: Option[Double] = None,
                   dx: Option[Double] = None,
                   dy: Option[Double] = None,
                   markerEnd: Option[String] = None,
                   stroke: Option[String] = None,
                   strokeWidth: Option[Double] = None,
                   style: Option[CSSProperties] = None,
                   subject: Option[Point] = None,
                   zoom: Option[Double] = None)

  override val component = ReactAnnotation
}