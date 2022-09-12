package com.harana.ui.external.rowsncolumns.spreadsheet

import com.harana.ui.external.rowsncolumns.SheetID
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/spreadsheet", "TabItem")
@js.native
object ReactTabItem extends js.Object

@react object TabItem extends ExternalComponent {

  case class Props(name: String,
                   isLight: Boolean,
                   isActive: Boolean,
                   id: SheetID,
                   onSelect: js.UndefOr[SheetID => Unit] = js.undefined,
                   onChangeSheetName: js.UndefOr[(SheetID, String) => Unit] = js.undefined,
                   onDeleteSheet: js.UndefOr[SheetID => Unit] = js.undefined,
                   onDuplicateSheet: js.UndefOr[SheetID => Unit] = js.undefined,
                   onHideSheet: js.UndefOr[SheetID => Unit] = js.undefined,
                   onProtectSheet: js.UndefOr[SheetID => Unit] = js.undefined,
                   onUnProtectSheet: js.UndefOr[SheetID => Unit] = js.undefined,
                   onChangeTabColor: js.UndefOr[(SheetID, js.UndefOr[String]) => Unit] = js.undefined,
                   locked: js.UndefOr[Boolean] = js.undefined,
                   canDelete: js.UndefOr[Boolean] = js.undefined,
                   canHide: js.UndefOr[Boolean] = js.undefined,
                   tabColor: js.UndefOr[String] = js.undefined)

  override val component = ReactTabItem
}