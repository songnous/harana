package com.harana.ui.external.rowsncolumns.spreadsheet

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/spreadsheet", "Tooltip")
@js.native
object ReactTooltip extends js.Object

@react object Tooltip extends ExternalComponent {

  case class Props(valid: js.UndefOr[Boolean] = js.undefined,
                   content: js.UndefOr[String] = js.undefined,
                   onMouseEnter: js.UndefOr[() => Unit] = js.undefined,
                   onMouseLeave: js.UndefOr[() => Unit] = js.undefined,
                   variant: js.UndefOr[TooltipVariant] = js.undefined,
                   position: js.UndefOr[TooltipPosition] = js.undefined)

  override val component = ReactTooltip
}