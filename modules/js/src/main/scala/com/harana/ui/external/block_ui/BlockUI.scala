package com.harana.ui.external.block_ui

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-block-ui", "BlockUi")
@js.native
object ReactBlockUI extends js.Object

@react object BlockUI extends ExternalComponent {

  case class Props(blocking: Option[Boolean],
                   keepInView: Option[Boolean],
                   className: Option[String],
                   message: Option[String],
                   loader: Option[String],
                   tag: Option[String])

  override val component = ReactBlockUI
}