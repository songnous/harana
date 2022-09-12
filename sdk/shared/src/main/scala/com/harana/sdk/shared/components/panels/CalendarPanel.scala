package com.harana.sdk.shared.components.panels

import java.time.Instant

import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.models.common.{Component, Event}
import io.circe.generic.JsonCodec
import enumeratum._

@JsonCodec
case class CalendarPanel(events: List[Event],
                         eventColors: List[(Event, String)],
                         leftActions: List[CalendarAction],
                         centerActions: List[CalendarAction],
                         rightActions: List[CalendarAction],
                         defaultView: CalendarView,
                         defaultTime: Instant,
                         viewLabels: List[(CalendarView, String)],
                         navigationLinks: Boolean,
                         editable: Boolean,
                         businessHours: Boolean,
                         eventLimit: Boolean) extends Component

sealed trait CalendarView extends EnumEntry
case object CalendarView extends Enum[CalendarView] with CirceEnum[CalendarView] {
  case object BasicDay extends CalendarView
  case object BasicWeek extends CalendarView
  case object AgendaDay extends CalendarView
  case object AgendaWeek extends CalendarView
  case object ListDay extends CalendarView
  case object ListWeek extends CalendarView
  case object ListMonth extends CalendarView
  case object ListYear extends CalendarView
  case object Month extends CalendarView
  val values = findValues
}

sealed trait CalendarAction extends EnumEntry
case object CalendarAction extends Enum[CalendarAction] with CirceEnum[CalendarAction] {
  case object BasicDayView extends CalendarAction
  case object BasicWeekView extends CalendarAction
  case object AgendaDayView extends CalendarAction
  case object AgendaWeekView extends CalendarAction
  case object ListDayView extends CalendarAction
  case object ListWeekView extends CalendarAction
  case object ListMonthView extends CalendarAction
  case object ListYearView extends CalendarAction
  case object MonthView extends CalendarAction
  case object Title extends CalendarAction
  case object Today extends CalendarAction
  case object Previous extends CalendarAction
  case object Next extends CalendarAction
  val values = findValues
}