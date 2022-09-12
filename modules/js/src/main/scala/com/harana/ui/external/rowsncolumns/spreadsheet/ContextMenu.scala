package com.harana.ui.external.rowsncolumns.spreadsheet

import com.harana.ui.external.rowsncolumns.grid.{CellInterface, SelectionArea}
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/spreadsheet", "ContextMenu")
@js.native
object ReactContextMenu extends js.Object

@react object ContextMenu extends ExternalComponent {

  type SelectionResult = (CellInterface, js.Array[SelectionArea]) => Unit

  case class Props(activeCell: js.UndefOr[CellInterface] = js.undefined,
                   selections: js.Array[SelectionArea],
                   onRequestClose: js.UndefOr[() => Unit] = js.undefined,
                   onInsertRow: js.UndefOr[SelectionResult] = js.undefined,
                   onDeleteRow: js.UndefOr[SelectionResult] = js.undefined,
                   onInsertColumn: js.UndefOr[SelectionResult] = js.undefined,
                   onDeleteColumn: js.UndefOr[SelectionResult] = js.undefined,
                   onCopy: js.UndefOr[SelectionResult] = js.undefined,
                   onCut: js.UndefOr[SelectionResult] = js.undefined,
                   onPaste: js.UndefOr[SelectionResult] = js.undefined,
                   onChange: js.UndefOr[SelectionResult] = js.undefined)

  override val component = ReactContextMenu
}