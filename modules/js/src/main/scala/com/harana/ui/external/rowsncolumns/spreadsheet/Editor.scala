package com.harana.ui.external.rowsncolumns.spreadsheet

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/spreadsheet", "Editor")
@js.native
object ReactEditor extends js.Object

@react object Editor extends ExternalComponent {

  case class Props(background: js.UndefOr[String] = js.undefined,
                   color: js.UndefOr[String] = js.undefined,
                   fontSize: js.UndefOr[Int] = js.undefined,
                   fontFamily: js.UndefOr[String] = js.undefined,
                   wrap: js.UndefOr[js.Any] = js.undefined,
                   horizontalAlign: js.UndefOr[js.Any] = js.undefined,
                   scale: js.UndefOr[Int] = js.undefined,
                   editorType: js.UndefOr[EditorType] = js.undefined,
                   options: js.Array[String] = js.Array(),
                   underline: js.UndefOr[Boolean] = js.undefined)

  override val component = ReactEditor
}