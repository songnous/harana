package com.harana.ui.external.button_loader

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-bootstrap-button-loader", JSImport.Default)
@js.native
object ReactBootstrapButtonLoader extends js.Object

@react object ButtonLoader extends ExternalComponent {

  case class Props(bsStyle: Option[String] = None,
                   children: List[ReactElement] = List(),
                   disabled: Option[Boolean] = None,
                   icon: Option[ReactElement] = None,
                   loading: Option[Boolean] = None,
                   spinColor: Option[String] = None,
                   spinAlignment: Option[String] = None,
                   variant: Option[String] = None)

  override val component = ReactBootstrapButtonLoader
}