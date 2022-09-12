package com.harana.ui.external.broadcast

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-broadcast", "Broadcast")
@js.native
object ReactBroadcast extends js.Object

@react object Broadcast extends ExternalComponent {

  case class Props(channel: String,
                   value: js.Any,
                   children: List[ReactElement])

  override val component = ReactBroadcast
}