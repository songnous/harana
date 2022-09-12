package com.harana.ui.external.rowsncolumns.spreadsheet

import com.harana.ui.external.rowsncolumns.SheetID
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/spreadsheet", "Tab")
@js.native
object ReactTab extends js.Object

@react object Tab extends ExternalComponent {

  case class Props(selectedSheet: SheetID,
                   sheets: js.Array[Sheet],
                   isTabEditable: js.UndefOr[Boolean] = js.undefined,
                   allowNewSheet: js.UndefOr[Boolean] = js.undefined,
                   onSelect: js.UndefOr[SheetID => Unit] = js.undefined,
                   onNewSheet: js.UndefOr[() => Unit] = js.undefined,
                   onChangeSheetName: js.UndefOr[(SheetID, String) => Unit] = js.undefined,
                   onDeleteSheet: js.UndefOr[SheetID => Unit] = js.undefined,
                   onDuplicateSheet: js.UndefOr[SheetID => Unit] = js.undefined,
                   onHideSheet: js.UndefOr[SheetID => Unit] = js.undefined,
                   onShowSheet: js.UndefOr[SheetID => Unit] = js.undefined,
                   onProtectSheet: js.UndefOr[SheetID => Unit] = js.undefined,
                   onUnProtectSheet: js.UndefOr[SheetID => Unit] = js.undefined,
                   onChangeTabColor: js.UndefOr[(SheetID, js.UndefOr[String]) => Unit] = js.undefined)

  override val component = ReactTab
}