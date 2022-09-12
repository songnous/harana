package com.harana.ui.external.rowsncolumns.spreadsheet.editor

import com.harana.ui.external.rowsncolumns.Direction
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-popconfirm", JSImport.Default)
@js.native
object ReactListEditor extends js.Object

@react object ListEditor extends ExternalComponent {

  case class Props(options: js.Array[String] = js.Array(),
                   value: String,
                   onChange: String => Unit,
                   onSubmit: (String, Direction) => Unit,
                   onCancel: () => Unit,
                   fontFamily: String,
                   fontSize: Int,
                   scale: Int,
                   color: String,
                   wrapping: js.Any,
                   horizontalAlign: js.Any)

  override val component = ReactListEditor
}