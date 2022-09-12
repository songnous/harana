package com.harana.ui.external.subscriber

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-broadcast", "Subscriber")
@js.native
object ReactSubcriber extends js.Object

@react object Subcriber extends ExternalComponent {

  case class Props(channel: String,
                   children: Option[js.Any => ReactElement] = None)

  override val component = ReactSubcriber
}