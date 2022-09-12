package com.harana.ui.external.rowsncolumns.spreadsheet

import com.harana.ui.external.rowsncolumns.SheetID
import com.harana.ui.external.rowsncolumns.grid.{CellInterface, SelectionArea}
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.ReactText
import typings.std.Promise

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/spreadsheet", "Spreadsheet")
@js.native
object ReactSpreadsheet extends js.Object

@react object Spreadsheet extends ExternalComponent {

  case class Props(minColumnWidth: js.UndefOr[Number] = js.undefined,
                   minRowHeight: js.UndefOr[Number] = js.undefined,
                   sheets: js.UndefOr[js.Array[Sheet]] = js.undefined,
                   initialSheets: js.UndefOr[js.Array[Sheet]] = js.undefined,
                   initialActiveSheet: js.UndefOr[String] = js.undefined,
                   onChangeCell: js.UndefOr[OnCellChange => Unit] = js.undefined,
                   onChangeSelectedSheet: js.UndefOr[SheetID => Unit] = js.undefined,
                   onChange: js.UndefOr[List[Sheet] => Unit] = js.undefined,
                   showFormulabar: js.UndefOr[Boolean] = js.undefined,
                   showToolbar: js.UndefOr[Boolean] = js.undefined,
                   formatter: js.UndefOr[Formatter] = js.undefined,
                   enableDarkMode: js.UndefOr[Boolean] = js.undefined,
                   fontFamily: js.UndefOr[String] = js.undefined,
                   minHeight: js.UndefOr[Number] = js.undefined,
                   selectionPolicy: js.UndefOr[SelectionPolicy] = js.undefined,
                   onActiveCellChange: js.UndefOr[OnCellChange => Unit] = js.undefined,
                   onActiveCellValueChange: js.UndefOr[OnCellChange => Unit] = js.undefined,
                   onSelectionChange: js.UndefOr[OnSelectionChange => Unit] = js.undefined,
                   selectionMode: js.UndefOr[SelectionMode] = js.undefined,
                   showTabStrip: js.UndefOr[Boolean] = js.undefined,
                   isTabEditable: js.UndefOr[Boolean] = js.undefined,
                   allowNewSheet: js.UndefOr[Boolean] = js.undefined,
                   showStatusBar: js.UndefOr[Boolean] = js.undefined,
                   initialScale: js.UndefOr[Number] = js.undefined,
                   onScaleChange: js.UndefOr[Number => Unit] = js.undefined,
                   fontLoaderConfig: js.UndefOr[js.Any] = js.undefined,
                   fontList: js.UndefOr[js.Array[String]] = js.undefined,
                   snap: js.UndefOr[Boolean] = js.undefined,
                   stateReducer: js.UndefOr[(StateInterface, js.Any) => StateInterface] = js.undefined,
                   onValidate: js.UndefOr[OnValidate => Promise[ValidationResponse]] = js.undefined,
                   enableGlobalKeyHandlers: js.UndefOr[Boolean] = js.undefined,
                   functions: js.UndefOr[Functions] = js.undefined,
                   //CellRenderer: js.UndefOr[React.ReactType] = js.undefined,
                   //HeaderCellRenderer: js.UndefOr[React.ReactType] = js.undefined,
                   //CellEditor: js.UndefOr[React.ReactType<CustomEditorProps>] = js.undefined,
                   //StatusBar: js.UndefOr[React.ReactType<StatusBarProps>] = js.undefined,
                   //ContextMenu: js.UndefOr[React.ReactType<ContextMenuComponentProps>] = js.undefined,
                   //Tooltip: js.UndefOr[React.ReactType<TooltipProps>] = js.undefined
                   )

  override val component = ReactSpreadsheet
}

trait OnCellChange extends js.Object {
  val id: SheetID
  val cell: js.UndefOr[CellInterface] = js.undefined
  val value: js.UndefOr[ReactText] = js.undefined
}

trait OnSelectionChange extends js.Object {
  val id: SheetID
  val activeCell: js.UndefOr[CellInterface] = js.undefined
  val selections: js.Array[SelectionArea]
}

trait OnValidate extends js.Object {
  val id: SheetID
  val cell: js.UndefOr[CellInterface] = js.undefined
  val value: js.UndefOr[ReactText] = js.undefined
  val cellConfig: js.UndefOr[CellConfig] = js.undefined
}

trait ValidationResponse extends js.Object {
  val valid: js.UndefOr[Boolean] = js.undefined
  val message: js.UndefOr[String] = js.undefined
}