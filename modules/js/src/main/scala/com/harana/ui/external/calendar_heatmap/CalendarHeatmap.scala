package com.harana.ui.external.calendar_heatmap

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.std.Date

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-calendar-heatmap", JSImport.Default)
@js.native
object ReactCalendarHeatmap extends js.Object

@react object CalendarHeatmap extends ExternalComponent {

  case class Props(classForValue: Option[js.Any => Unit] = None,
                   endDate: Option[String | Double | Date] = None,
                   gutterSize: Option[Double] = None,
                   horizontal: Option[Boolean] = None,
                   monthLabels: List[String] = List(),
                   numDays: Option[Double] = None,
                   onClick: Option[js.Any => Unit] = None,
                   onMouseLeave: Option[(js.Any, js.Any) => Unit] = None,
                   onMouseOver: Option[(js.Any, js.Any) => Unit] = None,
                   showMonthLabels: Option[Boolean] = None,
                   showOutOfRangeDays: Option[Boolean] = None,
                   showWeekdayLabels: Option[Boolean] = None,
                   startDate: Option[String | Double | Date] = None,
                   titleForValue: Option[js.Any => Unit] = None,
                   tooltipDataAttrs: Option[js.Object] = None,
                   transformDayElement: Option[(js.Any, js.Any, Double) => Unit] = None,
                   values: List[js.Any],
                   weekdayLabels: List[String] = List())

  override val component = ReactCalendarHeatmap
}