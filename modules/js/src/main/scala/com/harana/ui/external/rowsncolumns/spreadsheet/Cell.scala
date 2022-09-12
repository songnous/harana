package com.harana.ui.external.rowsncolumns.spreadsheet

import com.harana.ui.external.rowsncolumns.grid._
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/spreadsheet", "Cell")
@js.native
object ReactCell extends js.Object

@react object Cell extends ExternalComponent {

  case class Props(formatter: js.UndefOr[Formatter] = js.undefined,
                   showStrokeOnFill: js.UndefOr[Boolean] = js.undefined,
                   isSelected: js.UndefOr[Boolean] = js.undefined,
                   isLightMode: js.UndefOr[Boolean] = js.undefined,
                   showFilter: js.UndefOr[Boolean] = js.undefined,
                   isFilterActive: js.UndefOr[Boolean] = js.undefined,
                   onFilterClick: js.UndefOr[CellInterface => Unit] = js.undefined,
                   onEdit: js.UndefOr[CellInterface => Unit] = js.undefined,
                   onCheck: js.UndefOr[(CellInterface, Boolean) => Unit] = js.undefined,
                   cellConfig: js.UndefOr[CellConfig] = js.undefined)

  override val component = ReactCell
}