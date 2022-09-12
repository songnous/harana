package com.harana.ui.external.rowsncolumns.spreadsheet

import com.harana.ui.external.rowsncolumns.grid.CellInterface
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/spreadsheet", "Checkbox")
@js.native
object ReactCheckbox extends js.Object

@react object Checkbox extends ExternalComponent {

  case class Props(checked: Boolean,
                   rowIndex: Int,
                   columnIndex: Int,
                   onChange: js.UndefOr[(CellInterface, Boolean) => Unit] = js.undefined)

  override val component = ReactCheckbox
}