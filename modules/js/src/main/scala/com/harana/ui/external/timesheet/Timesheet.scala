package com.harana.ui.external.timesheet

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-timesheet", JSImport.Default)
@js.native
object ReactTimesheet extends js.Object

@react object Timesheet extends ExternalComponent {

  case class Props(data: List[TimesheetItem],
                   min: String,
                   max: String,
                   theme: Option[String] = None)

  override val component = ReactTimesheet
}

case class TimesheetItem(startDate: String, endDate: String, title: String, category: String)