package com.harana.ui.components.elements

import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}
import org.scalajs.dom.{UIEvent, window}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html.{div, id}

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.util.Random

@react class BarChart extends StatelessComponent {

  val elementId = s"barchart-${Random.nextInt(100000)}"
  val d3 = js.Dynamic.global.d3
  var componentWillResize: UIEvent => Unit = _

  case class Props(data: List[Int],
                   height: Int,
                   color: String,
                   tooltip: Option[String] = None,
                   animationEasingType: Option[BarChartAnimationEasingType] = None,
                   animationDuration: Option[Int] = None,
                   animationDelay: Option[Int] = None)

  override def componentDidMount(): Unit = {
    val jsArray = props.data.toJSArray
    val d3Container = d3.select(s"#$elementId")
    val width = Math.ceil(d3Container.node().getBoundingClientRect().width.asInstanceOf[Double])

    val delay = props.animationDelay.getOrElse(50)
    val duration = props.animationDuration.getOrElse(1200)
    val easing = props.animationEasingType.getOrElse(BarChartAnimationEasingType.Elastic).value

    val x = d3.scale.ordinal().rangeBands(js.Array(0, width), 0.3)
    val y = d3.scale.linear().range(js.Array(0, props.height))

    x.domain(d3.range(0, jsArray.length))
    y.domain(js.Array(0, d3.max(jsArray)))

    val container = d3Container.append("svg")
    val svg = container.attr("width", width).attr("height", props.height).append("g")

    svg.selectAll("rect")
      .data(jsArray)
      .enter()
      .append("rect")
      .attr("class", "d3-random-bars")
      .attr("width", x.rangeBand())
      .attr("x", (_: Int, i: Int) => x(i))
      .style("fill", props.color)
      .attr("height", 0)
      .attr("y", props.height)
      .transition()
      .attr("height", (d: Int) => y(d))
      .attr("y", (d: Int) => props.height - y(d).asInstanceOf[Double])
      .delay((_: Int, i: Int) => i * delay)
      .duration(duration)
      .ease(easing)

    componentWillResize = (_: UIEvent) => {
      val width = Math.ceil(d3Container.node().getBoundingClientRect().width.asInstanceOf[Double])
      container.attr("width", width)
      svg.attr("width", width)
      x.rangeBands(js.Array(0, width), 0.3)

      svg.selectAll(".d3-random-bars")
        .attr("width", x.rangeBand())
        .attr("x", (_: Int, i: Int) => x(i))
    }

    window.addEventListener("resize", componentWillResize)
    //document.on("click", ".sidebar-control", resizeBarChart(d3Container, container, svg, x))
  }

  override def componentWillUnmount(): Unit = {
    window.removeEventListener("resize", componentWillResize)
  }

  def render() =
    div(id := elementId)
}

sealed abstract class BarChartAnimationEasingType(val value: String) extends StringEnumEntry
case object BarChartAnimationEasingType extends StringEnum[BarChartAnimationEasingType] with StringCirceEnum[BarChartAnimationEasingType] {
  case object Elastic extends BarChartAnimationEasingType("elastic")
  val values = findValues
}