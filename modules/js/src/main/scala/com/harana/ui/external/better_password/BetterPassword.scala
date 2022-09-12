package com.harana.ui.external.better_password

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-better-password", JSImport.Namespace)
@js.native
object ReactBetterPassword extends js.Object

@react object BetterPassword extends ExternalComponent {

  case class Props(className: Option[String] = None,
                   mask: Option[String] = None,
                   onChange: Option[String => Unit] = None,
                   placeholder: Option[String] = None,
                   show: Option[Boolean] = None,
                   timeout: Option[Double] = None,
                   value: Option[String] = None)

  override val component = ReactBetterPassword
}