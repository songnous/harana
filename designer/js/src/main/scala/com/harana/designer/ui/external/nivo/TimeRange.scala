package com.harana.ui.external.nivo

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("@nivo/calendar", "TimeRange")
@js.native
object ReactTimeRange extends js.Object

@react object TimeRange extends ExternalComponent {

  case class Props(data: js.Array[CalendarEntry],
                   from: js.UndefOr[String | js.Date] = js.undefined,
                   to: js.UndefOr[String | js.Date] = js.undefined,
                   width: js.UndefOr[Int] = js.undefined,
                   height: js.UndefOr[Int] = js.undefined,
                   margin: js.UndefOr[Margin] = js.undefined,
                   direction: js.UndefOr[String] = js.undefined,
                   align: js.UndefOr[String] = js.undefined,
                   minValue: js.UndefOr[String | Int] = js.undefined,
                   maxValue: js.UndefOr[String | Int] = js.undefined,
                   colors: js.UndefOr[List[String]] = js.undefined,
                   emptyColor: js.UndefOr[String] = js.undefined,
                   monthLegend: js.UndefOr[(Int, Int, js.Date) => String | Int] = js.undefined,
                   monthLegendPosition: js.UndefOr[String] = js.undefined,
                   monthLegendOffset: js.UndefOr[Int] = js.undefined,
                   weekdayLegendOffset: js.UndefOr[Int] = js.undefined,
                   weekdayTickets: js.UndefOr[List[Int]] = js.undefined,
                   square: js.UndefOr[Boolean] = js.undefined,
                   dayRadius: js.UndefOr[Int] = js.undefined,
                   daySpacing: js.UndefOr[Int] = js.undefined,
                   dayBorderWidth: js.UndefOr[Int] = js.undefined,
                   dayBorderColor: js.UndefOr[String] = js.undefined,
                   isInteractive: js.UndefOr[Boolean] = js.undefined,
                   onClick: js.UndefOr[(Int, String) => Unit] = js.undefined,
                   legends: js.UndefOr[List[Legend]])

  override val component = ReactTimeRange
}