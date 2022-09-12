package com.harana.ui.external.big_calendar

import com.harana.ui.external.big_calendar.Types.{DateFormat, DateRangeFormat}
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-bigcalendar", JSImport.Default)
@js.native
object ReactBigCalendar extends js.Object

@react object BigCalendar extends ExternalComponent {

  case class Props(localizer: js.Object,
                   elementProps: Option[js.Object] = None,
                   date: Option[js.Date] = None,
                   view: Option[String] = None,
                   defaultView: Option[String] = None,
                   events: List[CalendarEvent],
                   titleAccessor: Option[String | CalendarEvent => String] = None,
                   tooltipAccessor: Option[String | CalendarEvent => String] = None,
                   allDayAccessor: Option[String | CalendarEvent => String] = None,
                   startAccessor: Option[String | CalendarEvent => js.Date] = None,
                   endAccessor: Option[String | CalendarEvent => js.Date] = None,
                   resourceAccessor: Option[String | CalendarEvent => js.Date] = None,
                   resources: List[Resource] = List(),
                   resourceIdAccessor: Option[String | Resource => js.Any] = None,
                   resourceTitleAccessor: Option[String | Resource => js.Any] = None,
                   getNow: Option[() => js.Date] = None,
                   onNavigate: Option[js.Object => Unit] = None,
                   onView: Option[js.Object => Unit] = None,
                   onDrillDown: Option[js.Object => Unit] = None,
                   onRangeChange: Option[js.Object => Unit] = None,
                   onSelectSlot: Option[SlotInfo => js.Any] = None,
                   onSelectEvent: Option[(CalendarEvent, CalendarEvent) => js.Any] = None,
                   onDoubleClickEvent: Option[(CalendarEvent, CalendarEvent) => Unit] = None,
                   onSelecting: Option[Range => Option[Boolean]] = None,
                   selected: Option[CalendarEvent] = None,
                   views: List[String] = List(),
                   drilldownView: Option[String] = None,
                   getDrilldownView: Option[(js.Date, String, List[String]) => String | Unit] = None,
                   length: Option[Int] = None,
                   toolbar: Option[Boolean] = None,
                   popup: Option[Boolean ] = None,
                   popupOffset: Option[Int | Position] = None,
                   selectTable: Option[Boolean | String] = None,
                   longPressThreshold: Option[Int] = None,
                   step: Option[Int] = None,
                   timeslots: Option[Int] = None,
                   rtl: Option[Boolean] = None,
                   eventPropGetter: Option[(CalendarEvent, js.Date, js.Date, Boolean) => Prop] = None,
                   slotPropGetter: Option[js.Date => Prop] = None,
                   dayPropGetter: Option[js.Date => Prop] = None,
                   showMultiDayTimes: Option[Boolean] = None,
                   min: Option[Int] = None,
                   max: Option[Int] = None,
                   scrollToTime: Option[Int] = None,
                   culture: Option[String] = None,
                   formats: Option[Formats] = None,
                   components: Option[js.Any] = None,
                   messages: Option[js.Any] = None)

  override val component = ReactBigCalendar
}

case class CalendarEvent(id: Int,
                         title: String,
                         start: js.Date,
                         end: js.Date,
                         allDay: Option[Boolean],
                         resourceId: Int)

case class Resource(resourceId: Int, resourceTitle: String)
case class SlotInfo(slotInfo: Slot)

case class Slot(start: js.Date,
                end: js.Date,
                slots: List[js.Date],
                action: String,
                bounds: Option[Bounds],
                box: Option[Box])

case class Bounds(x: Int, y: Int, top: Int, right: Int, left: Int, bottom: Int)
case class Box(clientX: Int, clientY: Int, x: Int, y: Int)
case class Range(start: js.Date, end: js.Date)
case class Position(x: Int, y: Int)
case class Prop(className: Option[String], style: Option[js.Object])

case class Formats(dateFormat: DateFormat,
                   dayFormat: DateFormat,
                   weekdayFormat: DateFormat,
                   timeGutterFormat: DateFormat,
                   monthHeaderFormat: DateFormat,
                   dayRangeHeaderFormat: DateRangeFormat,
                   dayHeaderFormat: DateFormat,
                   agendaHeaderFormat: DateFormat,
                   selectRangeFormat: DateRangeFormat,
                   agendaDateFormat: DateFormat,
                   agendaTimeFormat: DateFormat,
                   agendaTimeRangeFormat: DateRangeFormat,
                   eventTimeRangeFormat: DateRangeFormat,
                   eventTimeRangeStartFormat: DateRangeFormat,
                   eventTimeRangeEndFormat: DateRangeFormat)

object Types {
  type DateFormat = String | (js.Date, Option[String], js.Any) => String
  type DateRangeFormat = String | (Range, Option[String], js.Any) => String
}