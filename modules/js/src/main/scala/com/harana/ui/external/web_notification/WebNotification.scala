package com.harana.ui.external.video

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-web-notification", JSImport.Default)
@js.native
object ReactWebNotification extends js.Object

@react object WebNotification extends ExternalComponent {

  case class Props(
    ignore: Option[Boolean] = None,
    disableActiveWindow: Option[Boolean] = None,
    askAgain: Option[Boolean] = None,
    notSupported: Option[() => Unit] = None,
    onPermissionGranted: Option[() => Unit] = None,
    onPermissionDenied: Option[() => Unit] = None,
    onShow: Option[() => Unit] = None,
    onClick: Option[() => Unit] = None,
    onClose: Option[() => Unit] = None,
    onError: Option[() => Unit] = None,
    timeout: Option[Int] = None,
    title: String,
    options: Option[js.Object] = None,
    swRegistration: Option[js.Object] = None,
  )

  override val component = ReactWebNotification
}