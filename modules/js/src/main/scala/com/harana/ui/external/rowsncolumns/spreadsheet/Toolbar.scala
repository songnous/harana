package com.harana.ui.external.rowsncolumns.spreadsheet

import com.harana.ui.external.rowsncolumns.grid.Grid.{ColumnIndex, RowIndex}
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/spreadsheet", "Toolbar")
@js.native
object ReactToolbar extends js.Object

@react object Toolbar extends ExternalComponent {

  case class Props(onFormattingChange: js.UndefOr[js.Any => Unit] = js.undefined,
                   onFormattingChangeAuto: js.UndefOr[() => Unit] = js.undefined,
                   onFormattingChangePlain: js.UndefOr[() => Unit] = js.undefined,
                   onClearFormatting: js.UndefOr[() => Unit] = js.undefined,
                   onMergeCells: js.UndefOr[() => Unit] = js.undefined,
                   onFrozenColumnChange: js.UndefOr[ColumnIndex => Unit] = js.undefined,
                   onFrozenRowChange: js.UndefOr[RowIndex => Unit] = js.undefined,
                   frozenRows: js.UndefOr[Int] = js.undefined,
                   frozenColumns: js.UndefOr[Int] = js.undefined,
                   onBorderChange: js.UndefOr[OnBorderChange => Unit] = js.undefined,
                   canRedo: js.UndefOr[Boolean] = js.undefined,
                   canUndo: js.UndefOr[Boolean] = js.undefined,
                   onUndo: js.UndefOr[() => Unit] = js.undefined,
                   onRedo: js.UndefOr[() => Unit] = js.undefined,
                   enableDarkMode: js.UndefOr[Boolean] = js.undefined,
                   scale: js.UndefOr[Int] = js.undefined,
                   onScaleChange: js.UndefOr[Int => Unit] = js.undefined,
                   fontList: List[String] = List())

  override val component = ReactToolbar
}

trait OnBorderChange extends js.Object {
  val color: js.UndefOr[String] = js.undefined
  val borderStyle: String
  val variant: String
}