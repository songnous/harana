package com.harana.ui.external.simplemaps

import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-simple-maps", JSImport.Default)
@js.native
object ReactSimpleMaps extends js.Object

object SimpleMaps {
  type Point = (Double, Double)
  type ProjectionFunction = (Double, Double, ProjectionConfig) => js.Any
}

case class Default(default: Option[CSSProperties], hover: Option[CSSProperties], pressed: Option[CSSProperties])
case class End(end: Point, start: Point)
case class MarkerType(coordinates: Point)
case class Point(x: Point, y: Point)

case class ProjectionConfig(precision: Double,
                            rotation: (Double, Double, Double),
                            scale: Double,
                            xOffset: Double,
                            yOffset: Double)