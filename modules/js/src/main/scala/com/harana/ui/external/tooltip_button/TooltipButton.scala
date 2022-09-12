package com.harana.ui.external.tooltip_button

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-bootstrap-tooltip-button", JSImport.Default)
@js.native
object ReactBootstrapTooltipButton extends js.Object

@react object TooltipButton extends ExternalComponent {

  case class Props(disabled: Option[Boolean] = None,
                   title: Option[String] = None,
                   tooltipText: Option[String] = None,
                   tooltipId: Option[String] = None,
                   tooltipPlacement: Option[String] = None,
                   renderedButton: Option[ReactElement] = None,
                   onClick: Option[() => Unit],
                   bsStyle: Option[String] = None)

  override val component = ReactBootstrapTooltipButton
}