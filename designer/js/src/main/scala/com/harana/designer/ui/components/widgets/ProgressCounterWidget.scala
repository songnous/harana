package com.harana.ui.components.widgets

import org.scalajs.dom.window
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

import scala.scalajs.js
import scala.scalajs.js.timers.setTimeout
import scala.util.Random

@react class ProgressCounterWidget extends StatelessComponent {

	val elementId = s"progresscounter-${Random.nextInt(100000)}"

	case class Props(radius: Int,
									 border: Int,
									 color: String,
									 end: Double,
									 iconClass: String,
									 textTitle: String,
									 textAverage: String)

	override def componentDidMount(): Unit = {
		window.onload = { _ =>
			val d3 = js.Dynamic.global.d3
			val d3Container = d3.select(s"#$elementId")
			val container = d3Container.append("svg")

			val startPercent = 0.0
			val iconSize = 32
			val endPercent = props.end
			val twoPi = Math.PI * 2
			val formatPercent = d3.format(".0%")
			val boxSize = props.radius * 2

			var count = Math.abs((endPercent - startPercent) / 0.01)
			val step = if (endPercent < startPercent) -0.01 else 0.01

			val svg = container
				.attr("width", boxSize)
				.attr("height", boxSize)
				.append("g")
				.attr("transform", "translate(" + (boxSize / 2) + "," + (boxSize / 2) + ")")

			val arc = d3.svg.arc()
				.startAngle(0)
				.innerRadius(props.radius)
				.outerRadius(props.radius - props.border)

			svg.append("path")
				.attr("class", "d3-progress-background")
				.attr("d", arc.endAngle(twoPi))
				.style("fill", "#eee")

			val foreground = svg.append("path")
				.attr("class", "d3-progress-foreground")
				.attr("filter", "url(#blur)")
				.style("fill", props.color)
				.style("stroke", props.color)

			val front = svg.append("path")
				.attr("class", "d3-progress-front")
				.style("fill", props.color)
				.style("fill-opacity", 1)

			val numberText = d3.select(s"#$elementId")
				.append("h2")
				.attr("class", "mt-15 mb-5")

			d3.select(s"#$elementId")
				.append("i")
				.attr("class", props.iconClass + " counter-icon")
				.attr("style", "top: " + ((boxSize - iconSize) / 2) + "px")

			d3.select(s"#$elementId")
				.append("div")
				.text(props.textTitle)

			d3.select(s"#$elementId")
				.append("div")
				.attr("class", "text-size-small text-muted")
				.text(props.textAverage)

			var progress = startPercent

			def loop(): Unit = {
				foreground.attr("d", arc.endAngle(twoPi * progress))
				front.attr("d", arc.endAngle(twoPi * progress))
				numberText.text(formatPercent(progress))

				if (count > 0) {
					count -= 1
					progress += step
					setTimeout(10)(loop())
				}
			}
			loop()
		}
	}

	def render() =
		div(className := "panel text-center")(
			div(className := "panel-body")(
				div(className := "content-group-sm svg-center position-relative", id := elementId)
			)
		)
}