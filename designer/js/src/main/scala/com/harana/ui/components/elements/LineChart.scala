package com.harana.ui.components.elements

import org.scalajs.dom.{UIEvent, window}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html.{div, id}

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.JSNumberOps._
import scala.util.Random

@react class LineChart extends StatelessComponent {

  val elementId = s"linechart-${Random.nextInt(100000)}"
  val d3 = js.Dynamic.global.d3
  var componentWillResize: UIEvent => Unit = _

  case class Props(data: List[LineChartItem],
                   height: Int,
                   lineColor: String,
                   margin: Option[(Int, Int, Int, Int)] = None,
                   animationDuration: Option[Int] = None,
                   animationDelay: Option[Int] = None)

  override def componentDidMount(): Unit = {
    val data = props.data.toJSArray
    val d3Container = d3.select(s"#$elementId")

    val margin = props.margin.getOrElse((0, 0, 0, 0))
    val marginTop = margin._1
    val marginRight = margin._2
    val marginBottom = margin._3
    val marginLeft = margin._4

    val delay = props.animationDelay.getOrElse(150)
    val duration = props.animationDuration.getOrElse(1000)
    val width = Math.ceil(d3Container.node().getBoundingClientRect().width.asInstanceOf[Double]) - marginLeft - marginRight
    val height = props.height - marginTop - marginBottom
    val padding = 20

    val tooltip = d3.tip()
      .attr("class", "d3-tip")
      .html((d: LineChartItem) =>
        "<ul class='list-unstyled mb-5'>" +
          "<li>" + "<div class='text-size-base mt-5 mb-5'><i class='icon-check2 position-left'></i>" + d.dateTime + "</div>" + "</li>" +
          "<li>" + "Sales: &nbsp" + "<span class='text-semibold pull-right'>" + d.value + "</span>" + "</li>" +
          "<li>" + "Revenue: &nbsp " + "<span class='text-semibold pull-right'>" + "$" + (d.value * 25).toFixed(2) + "</span>" + "</li>" +
          "</ul>")

    val container = d3Container.append("svg")
    val svg = container
      .attr("width", width + marginLeft + marginRight)
      .attr("height", height + marginTop + marginBottom)
      .append("g")
      .attr("transform", s"translate($marginLeft, $marginTop)")
      .call(tooltip)

    val x = d3.time.scale().range(js.Array(padding.toDouble, width - padding.toDouble))
    val y = d3.scale.linear().range(js.Array(height, 5))

    x.domain(d3.extent(data, (d: LineChartItem) => d.dateTime))
    y.domain(js.Array(0, d3.max(data, (d: LineChartItem) => d.value)))

    val line = d3.svg.line()
      .x((d: LineChartItem) => x(d.dateTime))
      .y((d: LineChartItem) => y(d.value))

    val clip = svg.append("defs")
      .append("clipPath")
      .attr("id", "clip-line-small")

    val clipRect = clip.append("rect")
      .attr("class", "clip")
      .attr("width", 0)
      .attr("height", height)
      .transition()
      .duration(duration)
      .ease("linear")
      .attr("width", width)

    val path = svg.append("path")
      .attr("d", line(data))
      .attr("clip-path", "url(#clip-line-small)")
      .attr("class", "d3-line d3-line-medium")
      .style("stroke", props.lineColor)

    svg.select(".line-tickets")
      .transition()
      .duration(duration)
      .ease("linear")

    val guide = svg.append("g")
      .selectAll(".d3-line-guides-group")
      .data(data)

    guide
      .enter()
      .append("line")
      .attr("class", "d3-line-guides")
      .attr("x1", (d: LineChartItem, _: Any) => x(d.dateTime))
      .attr("y1", (_: Any, _: Any) => height)
      .attr("x2", (d: LineChartItem, _: Any) => x(d.dateTime))
      .attr("y2", (_: Any, _: Any) => height)
      .style("stroke", props.lineColor)
      .style("stroke-dasharray", "4,2")
      .style("shape-rendering", "crispEdges")

    guide
      .transition()
      .duration(duration)
      .delay((_: Any, i: Int) => i * delay)
      .attr("y2", (d: LineChartItem, _: Any) => y(d.  value))

    val points = svg.insert("g")
      .selectAll(".d3-line-circle")
      .data(data)
      .enter()
      .append("circle")
      .attr("class", "d3-line-circle d3-line-circle-medium")
      .attr("cx", line.x())
      .attr("cy", line.y())
      .attr("r", 3)
      .style("stroke", props.lineColor)
      .style("fill", props.lineColor)
      .style("opacity", 0)
      .transition()
      .duration(250)
      .ease("linear")
      .delay(1000)
      .style("opacity", 1)

//    points
//      .on("mouseover", (d: LineChartItem) => {
//        tooltip.offset(js.Array(-10, 0)).show(d)
//        d3.select(this).transition().duration(250).attr("r", 4)
//      })
//
//      .on("mouseout", (d: LineChartItem) => {
//        tooltip.hide(d)
//        d3.select(this).transition().duration(250).attr("r", 3)
//      })
//
//    d3.select(points(0)(0))
//      .on("mouseover", (d: LineChartItem) => {
//        tooltip.offset(js.Array(0, 10)).direction("e").show(d)
//        d3.select(this).transition().duration(250).attr("r", 4)
//      })
//
//      .on("mouseout", (d: LineChartItem) => {
//        tooltip.direction("n").hide(d)
//        d3.select(this).transition().duration(250).attr("r", 3)
//      })
//
//    d3.select(points(0)(points.size().asInstanceOf[Int] - 1))
//      .on("mouseover", (d: LineChartItem) => {
//        tooltip.offset(js.Array(0, -10)).direction("w").show(d)
//        d3.select(this).transition().duration(250).attr("r", 4)
//      })
//
//      .on("mouseout", (d: LineChartItem) => {
//        tooltip.direction("n").hide(d)
//        d3.select(this).transition().duration(250).attr("r", 3)
//      })

    componentWillResize = (_: UIEvent) => {
      val width = Math.ceil(d3Container.node().getBoundingClientRect().width.asInstanceOf[Double]) - marginLeft - marginRight
      container.attr("width", width + marginLeft + marginRight)
      svg.attr("width", width + marginLeft + marginRight)
      x.range(js.Array(padding.toDouble, width - padding.toDouble))

      clipRect.attr("width", width)
      svg.selectAll(".d3-line").attr("d", line(data))
      svg.selectAll(".d3-line-circle").attr("cx", line.x())

      svg.selectAll(".d3-line-guides")
        .attr("x1", (d: LineChartItem, _: js.Any) => x(d.dateTime))
        .attr("x2", (d: LineChartItem, _: js.Any) => x(d.dateTime))
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

case class LineChartItem(dateTime: js.Date, value: Int)