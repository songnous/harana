package com.harana.ui.external.time_ago

import com.harana.ui.external.time_ago.Types.Formatter
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-timeago", JSImport.Default)
@js.native
object ReactTimeAgo extends js.Object

@react object TimeAgo extends ExternalComponent {

  case class Props(date: js.Date | Long | String,
                   live: Option[Boolean] = None,
                   minPeriod: Option[Long] = None,
                   maxPeriod: Option[Long] = None,
                   component: Option[String] = None,
                   title: Option[String] = None,
                   formatter: Option[Formatter] = None)

  override val component = ReactTimeAgo
}

object Types {
  type Formatter = (Int, String, String, Int) => Unit
}