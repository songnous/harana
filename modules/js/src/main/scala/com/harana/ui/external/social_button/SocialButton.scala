package com.harana.ui.external.social_button

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-social-button", JSImport.Default)
@js.native
object ReactSocialButton extends js.Object

@react object SocialButton extends ExternalComponent {

  case class Props(
    social: String,
    text: Option[String] = None,
    loading: Option[Boolean] = None,
    btnProps: Option[js.Object] = None
  )

  override val component = ReactSocialButton
}