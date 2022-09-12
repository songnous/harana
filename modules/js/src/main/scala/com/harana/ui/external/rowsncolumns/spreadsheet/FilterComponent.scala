package com.harana.ui.external.rowsncolumns.spreadsheet

import com.harana.ui.external.rowsncolumns.grid.CellPosition
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.ReactText

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/spreadsheet", "FilterComponent")
@js.native
object ReactFilterComponent extends js.Object

@react object FilterComponent extends ExternalComponent {

  case class Props(position: CellPosition,
                   onChange: js.UndefOr[OnFilterComponentChange => Unit] = js.undefined,
                   onCancel: js.UndefOr[() => Unit] = js.undefined,
                   values: js.Array[ReactText],
                   filter: FilterDefinition,
                   columnIndex: js.UndefOr[Int] = js.undefined,
                   index: Int,
                   width: Int)

  override val component = ReactFilterComponent
}

trait OnFilterComponentChange extends js.Object {
  val filterIndex: Int
  val columnIndex: Int
  val filter: js.UndefOr[FilterDefinition] = js.undefined
}