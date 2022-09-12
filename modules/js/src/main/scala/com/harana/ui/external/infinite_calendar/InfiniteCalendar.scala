package com.harana.ui.external.infinite_calendar

import com.harana.ui.external.infinite_calendar.Types._
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.std.Date

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-infinite-calendar", JSImport.Default)
@js.native
object ReactInfiniteCalendar extends js.Object

@react object InfiniteCalendar extends ExternalComponent {

  case class Props(autoFocus: Option[Boolean] = None,
                   className: Option[String] = None,
                   disabledDates: List[DateType] = List(),
                   disabledDays: List[Int] = List(),
                   display: Option[String] = None,
                   displayOptions: Option[HideYearsOnSelect] = None,
                   height: Option[Double | String] = None,
                   locale: Option[Locale] = None,
                   max: Option[DateType] = None,
                   maxDate: Option[DateType] = None,
                   min: Option[DateType] = None,
                   minDate: Option[DateType] = None,
                   onScroll: Option[Double => Unit] = None,
                   onScrollEnd: Option[Double => Unit] = None,
                   onSelect: Option[DateSelectFunction | RangedSelectFunction] = None,
                   rowHeight: Option[Double] = None,
                   selected: Option[DateType | Boolean | End] = None,
                   tabIndex: Option[Double] = None,
                   theme: Option[AccentColor] = None,
                   width: Option[Double | String] = None)

  override val component = ReactInfiniteCalendar
}

object Types {
  type DateSelectFunction = Date => Unit
  type DateType = Date | String | Double
  type RangedSelectFunction = RangedSelection => Unit
}

case class AccentColor(accentColor: Option[String],
                       floatingNav: Option[Background],
                       headerColor: Option[String],
                       selectionColor: Option[String],
                       textColor: Option[Active],
                       todayColor: Option[String],
                       weekdayColor: Option[String])

case class Active(active: Option[String], default: Option[String])
case class Background(background: Option[String], chevron: Option[String], color: Option[String])
case class End(end: DateType, start: DateType)

case class HideYearsOnSelect(hideYearsOnSelect: Option[Boolean],
                             layout: Option[String],
                             overscanMonthCount: Option[Double],
                             shouldHeaderAnimate: Option[Boolean],
                             showHeader: Option[Boolean],
                             showMonthsForYears: Option[Boolean],
                             showOverlay: Option[Boolean],
                             showTodayHelper: Option[Boolean],
                             showWeekdays: Option[Boolean],
                             todayHelperRowOffset: Option[Double])

case class Locale(blank: Option[String],
                  headerFormat: Option[String],
                  todayLabel: Option[Long],
                  weekStartsOn: Option[Int],
                  weekdays: List[String])

case class Long(long: String)
case class RangedSelection(end: Date, eventType: Int, start: Date)