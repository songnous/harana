package com.harana.ui.external.rowsncolumns.spreadsheet

import com.harana.ui.external.rowsncolumns.grid.SelectionArea
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/spreadsheet", "StatusBar")
@js.native
object ReactStatusBar extends js.Object

@react object StatusBar extends ExternalComponent {

  case class Props(selections: js.Array[SelectionArea],
                  cells: Cells)

  override val component = ReactStatusBar
}