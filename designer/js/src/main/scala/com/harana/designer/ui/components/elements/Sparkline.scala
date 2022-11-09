package com.harana.ui.components.elements

import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}
import org.scalajs.dom.{UIEvent, window}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.timers.setInterval
import scala.util.Random

@react class Sparkline extends StatelessComponent {

	val elementId = s"sparkline-${Random.nextInt(100000)}"
	val d3 = js.Dynamic.global.d3
	var componentWillResize: UIEvent => Unit = _

	case class Props(data: List[Int],
									 chartType: SparklineChartType,
									 height: Int,
									 color: String,
									 interpolationType: Option[SparklineInterpolationType] = None,
									 duration: Option[Int] = None,
									 interval: Option[Int] = None,
									 margin: Option[(Int, Int, Int, Int)] = None)

	override def componentDidMount(): Unit = {
		val jsArray = props.data.toJSArray
		val quantity = jsArray.size
		val d3 = js.Dynamic.global.d3

		val interval = props.interval.getOrElse(1000)
		val duration = props.duration.getOrElse(750)
		val interpolationType = props.interpolationType.getOrElse(SparklineInterpolationType.Basis).value

		val d3Container = d3.select(s"#$elementId")
		val margin = props.margin.getOrElse((0, 0, 0, 0))
		val marginTop = margin._1
		val marginRight = margin._2
		val marginBottom = margin._3
		val marginLeft = margin._4

		val width = Math.ceil(d3Container.node().getBoundingClientRect().width.asInstanceOf[Double]) - marginLeft - marginRight
		val height = props.height - marginTop - marginBottom

		val x = d3.scale.linear().range(js.Array(0, width))
		val y = d3.scale.linear().range(js.Array(props.height - 5, 5))

		x.domain(js.Array(1, quantity - 3))
		y.domain(js.Array(0, quantity))

		val line = d3.svg.line()
			.interpolate(interpolationType)
			.x((_: Int, i: Int) => x(i))
			.y((d: Int, _: Int) => y(d))

		val area = d3.svg.area()
			.interpolate(interpolationType)
			.x((_: Int, i: Int) => x(i))
			.y0(props.height)
			.y1((d: Int, _: Int) => y(d))

		val container = d3Container.append("svg")

		val svg = container
			.attr("width", width + marginLeft + marginRight)
			.attr("height", height + marginTop + marginBottom)
			.append("g")
			.attr("transform", "translate(" + marginLeft + "," + marginTop + ")")

		val clip = svg.append("defs")
			.append("clipPath")
			.attr("id", (_: Any, _: Any) => s"load-clip-$elementId")

		val clips = clip.append("rect")
			.attr("class", "load-clip")
			.attr("width", 0)
			.attr("height", height)
			.transition()
			.duration(1000)
			.ease("linear")
			.attr("width", width)

		val path = svg.append("g")
			.attr("clip-path", (_: Any, _: Any) => s"url(#load-clip-$elementId)")
			.append("path")
			.datum(jsArray)
			.attr("transform", s"translate(${x(0)},0)")

		props.chartType match {
			case SparklineChartType.Area =>
				path.attr("d", area).attr("class", "d3-area").style("fill", props.color)

			case SparklineChartType.Line =>
				path.attr("d", line).attr("class", "d3-line d3-line-medium").style("stroke", props.color)
		}

		path
			.style("opacity", 0)
			.transition()
			.duration(750)
			.style("opacity", 1)

		setInterval(interval.toDouble)({
			jsArray.push(10)
			jsArray.shift()

			path
				.attr("transform", null)
				.transition()
				.duration(duration)
				.ease("linear")
				.attr("transform", "translate(" + x(0) + ",0)")

			props.chartType match {
				case SparklineChartType.Area =>
					path.attr("d", area).attr("class", "d3-area").style("fill", props.color)

				case SparklineChartType.Line =>
					path.attr("d", line).attr("class", "d3-line d3-line-medium").style("stroke", props.color)
			}
		})

		componentWillResize = (_: UIEvent) => {
			val width = Math.ceil(d3Container.node().getBoundingClientRect().width.asInstanceOf[Double]) - marginLeft - marginRight
			container.attr("width", width + marginLeft + marginRight)
			svg.attr("width", width + marginLeft + marginRight)
			x.range(js.Array(0, width))
			clips.attr("width", width)
			svg.select(".d3-line").attr("d", line)
			svg.select(".d3-area").attr("d", area)
		}

		window.addEventListener("resize", componentWillResize)
		//$(document).on("click', '.sidebar-control', revenueResize)
	}

	override def componentWillUnmount(): Unit = {
		window.removeEventListener("resize", componentWillResize)
	}

	def render() =
		div(id := elementId)
}

sealed abstract class SparklineChartType(val value: String) extends StringEnumEntry
case object SparklineChartType extends StringEnum[SparklineChartType] with StringCirceEnum[SparklineChartType] {
	case object Area extends SparklineChartType("area")
	case object Line extends SparklineChartType("line")
	val values = findValues
}

sealed abstract class SparklineInterpolationType(val value: String) extends StringEnumEntry
case object SparklineInterpolationType extends StringEnum[SparklineInterpolationType] with StringCirceEnum[SparklineInterpolationType] {
	case object Basis extends SparklineInterpolationType("basis")
	val values = findValues
}