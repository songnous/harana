package com.harana.ui.external.navbar_offcanvas

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-bootstrap-navbar-offcanvas", JSImport.Default)
@js.native
object ReactBootstrapNavbarOffCanvas extends js.Object

@react object NavbarOffCanvas extends ExternalComponent {

  case class Props(
    in: Option[Boolean] = None,
    side: Option[String] = None
  )

  override val component = ReactBootstrapNavbarOffCanvas
}