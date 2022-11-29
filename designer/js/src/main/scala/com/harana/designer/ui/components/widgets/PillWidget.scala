package com.harana.ui.components.widgets

import com.harana.designer.frontend.utils.ColorUtils
import com.harana.sdk.shared.models.common.Background
import com.harana.ui.components._
import com.harana.ui.components.elements._
import com.harana.ui.components.LinkType
import com.harana.ui.external.shoelace.{Button, Dropdown, Menu}
import enumeratum.{CirceEnum, Enum, EnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._
import org.scalajs.dom
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.util.Random

@react class PillWidget extends StatelessComponent {

	case class Props(title: String,
									 subtitle: Option[String],
									 chartType: Option[PillChartType],
									 link: Option[LinkType],
									 background: Option[Background],
									 menuItems: List[ReactElement] = List())

	def render() =
		div(className := cssSet("pill panel" -> true), style := ColorUtils.css(props.background))(
			div(className := "heading-elements pill-heading-elements")(
				span(className := cssSet(
					"heading-text badge" -> true
				))(
					when(props.menuItems.nonEmpty,
						Dropdown(
							button = Some(Button.Props(icon = Some("icomoon","menu7"), className = Some("pill-dropdown"), caret = Some(true))),
							menu = if (props.menuItems.isEmpty) None else Some(Menu.Props(items = props.menuItems)),
						).withKey(Random.nextInt().toString)
					)
				)
			),
			div(className := "panel-body", onClick := (event => if (props.link.isDefined) openLink(props.link.get)))(
				h3(className := "heading-text")(span(props.title)),
				props.subtitle
			),
			div(className := "container-fluid", onClick := (event => if (props.link.isDefined) openLink(props.link.get)))(
				props.chartType match {

					case Some(PillChartType.Bar) =>
						div()
//						BarChart(x.toList, 50, "rgba(255,255,255,0.5)", Some("members"))

					case Some(PillChartType.Line) =>
						val a = LineChartItem(new js.Date("Mon, 25 Dec 1995 09:30:00 GMT"), 10)
						val b = LineChartItem(new js.Date("Mon, 25 Dec 1995 10:30:00 GMT"), 20)
						val c = LineChartItem(new js.Date("Mon, 25 Dec 1995 12:30:00 GMT"), 30)
						val d = LineChartItem(new js.Date("Mon, 25 Dec 1995 13:30:00 GMT"), 10)
						LineChart(List(a, b, c, d), 50, "rgba(255,255,255,0.5)")

					case Some(PillChartType.Sparkline) =>
						Sparkline(Seq.fill(100)(Random.nextInt(100)).toList, SparklineChartType.Area, 50, "rgba(255,255,255,0.5)")

					case None => span()
				}
			)
		)
}

sealed trait PillChartType extends EnumEntry
case object PillChartType extends Enum[PillChartType] with CirceEnum[PillChartType] {
	case object Bar extends PillChartType
	case object Line extends PillChartType
	case object Sparkline extends PillChartType
	val values = findValues
}