package com.harana.ui.external.dialog

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-bootstrap-dialog", JSImport.Default)
@js.native
object ReactBootstrapDialog extends js.Object

@react object Dialog extends ExternalComponent {

  case class Props()

  override val component = ReactBootstrapDialog
}