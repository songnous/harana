package com.harana.ui.external.flexr_grid

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-flexr", "Grid")
@js.native
object ReactFlexrGrid extends js.Object

@react object FlexrGrid extends ExternalComponent {

  case class Props(align: Option[String],
                   flexCells: Option[Boolean] = None,
                   gutter: Option[String] = None,
                   hAlign: Option[String])

  override val component = ReactFlexrGrid
}