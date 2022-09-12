package com.harana.ui.external.notifier

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-bs-notifier", "Alert")
@js.native
object ReactBSNotifierAlertTimer extends js.Object

@react object NotifierAlertTimer extends ExternalComponent {

  case class Props(`type`: Option[String] = None,
                   headline: Option[String] = None,
                   onDismiss: Option[() => Unit] = None,
                   dismissTitle: Option[String] = None,
                   showIcon: Option[Boolean] = None,
                   timeout: Option[Int] = None)

  override val component = ReactBSNotifierAlertTimer
}

@JSImport("react-bs-notifier", "AlertContainer")
@js.native
object ReactBSNotifierAlertContainer extends js.Object

@react object NotifierAlertContainer extends ExternalComponent {
  case class Props(position: Option[String] = None)
  override val component = ReactBSNotifierAlertContainer
}

@JSImport("react-bs-notifier", "AlertList")
@js.native
object ReactBSNotifierAlertList extends js.Object

@react object NotifierAlertList extends ExternalComponent {

  case class Props(alerts: List[Alert] = List(),
                   position: Option[String] = None,
                   onDismiss: Option[() => Unit] = None,
                   timeout: Option[Int] = None)

  override val component = ReactBSNotifierAlertList
}

@js.native
trait Alert extends js.Object {
  val id: String = js.native
  val `type`: Option[String] = js.native
  val headline: Option[String] = js.native
  val message: Option[String] = js.native
}
