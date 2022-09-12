package com.harana.ui.external.rowsncolumns.spreadsheet

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.{ChangeEvent, FocusEvent, KeyboardEvent}
import typings.std.HTMLInputElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/spreadsheet", "Formulabar")
@js.native
object ReactFormulabar extends js.Object

@react object Formulabar extends ExternalComponent {

  case class Props(onChange: js.UndefOr[ChangeEvent[HTMLInputElement] => Unit] = js.undefined,
                   onKeyDown: js.UndefOr[KeyboardEvent[HTMLInputElement] => Unit] = js.undefined,
                   onFocus: js.UndefOr[FocusEvent[HTMLInputElement] => Unit] = js.undefined,
                   onBlur: js.UndefOr[FocusEvent[HTMLInputElement] => Unit] = js.undefined,
                   value: String)

  override val component = ReactFormulabar
}