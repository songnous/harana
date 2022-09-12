package com.harana.ui.external.rowsncolumns.grid

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/grid", "Cell")
@js.native
object ReactCell extends js.Object

@react object Cell extends ExternalComponent {

  case class Props(value: js.UndefOr[String] = js.undefined,
                   textColor: js.UndefOr[String] = js.undefined,
                   padding: js.UndefOr[Number] = js.undefined,
                   fontWeight: js.UndefOr[String] = js.undefined,
                   fontStyle: js.UndefOr[String] = js.undefined,
                   onClick: js.UndefOr[js.Any => Unit] = js.undefined)

  override val component = ReactCell
}