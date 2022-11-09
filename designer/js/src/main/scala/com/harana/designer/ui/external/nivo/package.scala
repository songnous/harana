package com.harana.ui.external

import scala.scalajs.js
import scala.scalajs.js.|

package object nivo {

  trait CalendarEntry extends js.Object {
    val day : js.UndefOr[String] = js.undefined
    val value: js.UndefOr[Int] = js.undefined
  }

  trait Colors extends js.Object {
    val scheme : js.UndefOr[String] = js.undefined
  }

  trait WaffleEntry extends js.Object {
    val id : js.UndefOr[Int | String] = js.undefined
    val value: js.UndefOr[Int] = js.undefined
    val label: js.UndefOr[Int | String] = js.undefined
  }

  trait Legend extends js.Object {
    val anchor: js.UndefOr[String] = js.undefined
    val direction: js.UndefOr[String] = js.undefined
    val justify: js.UndefOr[Boolean] = js.undefined
    val translateX: js.UndefOr[String] = js.undefined
    val translateY: js.UndefOr[String] = js.undefined
    val itemWidth: js.UndefOr[Int] = js.undefined
    val itemHeight: js.UndefOr[Int] = js.undefined
    val itemsSpacing: js.UndefOr[Int] = js.undefined
    val itemDirection: js.UndefOr[String] = js.undefined
    val itemOpacity: js.UndefOr[Int] = js.undefined
    val itemTextColor: js.UndefOr[String] = js.undefined
    val symbolSize: js.UndefOr[Int] = js.undefined
  }

  trait Margin extends js.Object {
    val top : js.UndefOr[Int] = js.undefined
    val right : js.UndefOr[Int] = js.undefined
    val bottom : js.UndefOr[Int] = js.undefined
    val left : js.UndefOr[Int] = js.undefined
  }

  trait BorderColor extends js.Object {
    val from : js.UndefOr[String] = js.undefined
    val modifiers : js.UndefOr[List[ThemeModifier]] = js.undefined
  }

  trait ThemeModifier extends js.Object {
    val modification: js.UndefOr[String] = js.undefined
    val amount: js.UndefOr[Int] = js.undefined
  }

  trait TooltipData extends js.Object {
    val id:         js.UndefOr[String | Int] = js.undefined
    val value:      js.UndefOr[Int] = js.undefined
    val label:      js.UndefOr[String | Int] = js.undefined
    val color:      js.UndefOr[String] = js.undefined
    val position:   js.UndefOr[Int] = js.undefined
    val row:        js.UndefOr[Int] = js.undefined
    val column:     js.UndefOr[Int] = js.undefined
    val groupIndex: js.UndefOr[Int] = js.undefined
    val startAt:    js.UndefOr[Int] = js.undefined
    val endAt:      js.UndefOr[Int] = js.undefined
  }
}