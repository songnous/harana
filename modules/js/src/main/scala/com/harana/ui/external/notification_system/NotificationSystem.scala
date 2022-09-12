package com.harana.ui.external.notification_system

import com.harana.ui.external.notification_system.Types.CallBackFunction
import org.scalajs.dom.HTMLElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-notification-system", JSImport.Namespace)
@js.native
object ReactNotificationSystem extends js.Object {
  def addNotification(notification: Notification): Notification = js.native
  def clearNotifications(): Unit = js.native
  def editNotification(uidOrNotification: String, newNotification: Notification): Unit = js.native
  def editNotification(uidOrNotification: Notification, newNotification: Notification): Unit = js.native
  def editNotification(uidOrNotification: Double, newNotification: Notification): Unit = js.native
  def removeNotification(uidOrNotification: String): Unit = js.native
  def removeNotification(uidOrNotification: Notification): Unit = js.native
  def removeNotification(uidOrNotification: Double): Unit = js.native
}

@react object NotificationSystem extends ExternalComponent {

  case class Props(allowHTML: Option[Boolean],
                   noAnimation: Option[Boolean],
                   style: Option[Style | Boolean])

  override val component = ReactNotificationSystem
}

object Types {
  type CallBackFunction = Notification => Unit
}

case class ActionObject(callback: Option[() => Unit], label: String)

case class ContainersStyle(DefaultStyle: CSSProperties,
                           bc: Option[CSSProperties],
                           bl: Option[CSSProperties],
                           br: Option[CSSProperties],
                           tc: Option[CSSProperties],
                           tl: Option[CSSProperties],
                           tr: Option[CSSProperties])

case class ItemStyle(DefaultStyle: Option[CSSProperties],
                     error: Option[CSSProperties],
                     info: Option[CSSProperties],
                     success: Option[CSSProperties],
                     warning: Option[CSSProperties])

case class Notification(action: Option[ActionObject],
                        autoDismiss: Option[Double],
                        children: Option[ReactElement],
                        dismissible: Option[Boolean],
                        level: Option[String],
                        message: Option[String | HTMLElement],
                        onAdd: Option[CallBackFunction],
                        onRemove: Option[CallBackFunction],
                        position: Option[String],
                        title: Option[String | HTMLElement],
                        uid: Option[Double | String])

case class State(notifications: List[Notification])

case class Style(Action: Option[ItemStyle],
                 ActionWrapper: Option[WrapperStyle],
                 Containers: Option[ContainersStyle],
                 Dismiss: Option[ItemStyle],
                 MessageWrapper: Option[WrapperStyle],
                 NotificationItem: Option[ItemStyle],
                 Title: Option[ItemStyle],
                 Wrapper: Option[js.Any])

case class WrapperStyle(DefaultStyle: Option[CSSProperties])