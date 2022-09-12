package com.harana.ui.external.rowsncolumns.spreadsheet

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/spreadsheet", "HeaderCell")
@js.native
object ReactHeaderCell extends js.Object

@react object HeaderCell extends ExternalComponent {

  type Direction = String

  case class Props(value: String,
                   onChange: String => Unit,
                   onSubmit: (String, Direction) => Unit,
                   onCancel: () => Unit,
                   fontFamily: String,
                   fontSize: Int,
                   scale: Int,
                   color: String,
                   wrapping: js.Any,
                   horizontalAlign: js.Any,
                   underline: Boolean)

  override val component = ReactHeaderCell
}