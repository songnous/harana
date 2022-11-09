package com.harana.ui.components.table

import com.harana.sdk.shared.utils.Random
import com.harana.ui.external.nivo.{TooltipData, Waffle, WaffleEntry}
import slinky.core.{ReactComponentClass, StatelessComponent}
import slinky.core.annotations.react
import slinky.web.html._
import slinky.core.ReactElementMod

import scala.scalajs.js
import scala.scalajs.js._
import scala.scalajs.js.annotation.JSExportTopLevel

@react class HistoryColumn extends StatelessComponent {

	case class Props(values: List[(Int, String)],
									 columns: Int)

	def render() =
		div(className := "temp-row-height")(
			Waffle(
				total = props.values.size,
				data = props.values.map { v => new WaffleEntry {
					override val id = Random.short
					override val value = v._1
					override val label = v._2
				}},
				columns = props.columns,
				rows = 1,
				height = 40,
				fillDirection = "left",
				width = 10 * props.values.size
			)
		)
}