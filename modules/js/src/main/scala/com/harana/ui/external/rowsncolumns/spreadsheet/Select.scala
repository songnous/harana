package com.harana.ui.external.rowsncolumns.spreadsheet

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("@rowsncolumns/spreadsheet", "Select")
@js.native
object ReactSelect extends js.Object

@react object Select extends ExternalComponent {

  case class Props(showInput: js.UndefOr[Boolean] = js.undefined,
                   options: js.Array[SelectOption],
                   value: js.UndefOr[SelectOption] = js.undefined,
                   onChange: js.UndefOr[js.UndefOr[SelectOption] => Unit] = js.undefined,
                   format: js.UndefOr[String => Unit] = js.undefined,
                   inputWidth: js.UndefOr[Int]= js.undefined,
                   enableInput: js.UndefOr[Boolean]= js.undefined,
                   id: js.UndefOr[String]= js.undefined)

  override val component = ReactSelect
}

trait SelectOption extends js.Object {
  val value: String | Int
  val label: String | Int
}