  package com.harana.ui.components.elements

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html.{className, div, id}

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.JSON
import scala.util.Random
import scala.scalajs.js.timers._

@react class MultipleProgressChart extends StatelessComponent {

  val elementId = s"multipleprogresschart-${Random.nextInt(100000)}"
  val d3 = js.Dynamic.global.d3

  case class Props(data: List[MultipleProgressChartItem])

  override def componentDidMount(): Unit = {
    val jsArray = props.data.toJSArray
    val d3Container = d3.select(s"#$elementId")
    val padding = 2
    val strokeWidth = 8
    val size = 200
    val angle = 2 * Math.PI

    val container = d3Container.append("svg")

    val svg = container
      .attr("width", size)
      .attr("height", size)
      .append("g")
      .attr("transform", s"translate(${size / 2},${size / 2})")

    val arc = d3.svg.arc()
      .startAngle(0)
      .endAngle((d: MultipleProgressChartItem) => d.percentage / 100 * angle)
      .innerRadius((d: MultipleProgressChartItem, i: Int) => (size / 2) - i * (strokeWidth + padding))
      .outerRadius((d: MultipleProgressChartItem, i: Int) => ((size / 2) - i * (strokeWidth + padding)) - strokeWidth)
      .cornerRadius(20)

    val background = d3.svg.arc()
      .startAngle(0)
      .endAngle(angle)
      .innerRadius((d: MultipleProgressChartItem, i: Int) => (size / 2) - i * (strokeWidth + padding))
      .outerRadius((d: MultipleProgressChartItem, i: Int) => ((size / 2) - i * (strokeWidth + padding)) - strokeWidth)

    var field = svg.selectAll("g")
      .data(jsArray)
      .enter().append("g")

    field
      .append("path")
      .attr("class", "arc-foreground")
      .style("fill", (d: MultipleProgressChartItem, _: Int) => d.color)

    field
      .append("path")
      .style("fill", (d: MultipleProgressChartItem, _: Int) => d.color)
      .style("opacity", 0.1)
      .attr("d", background)

    val legend = d3Container
      .append("ul")
      .attr("class", "chart-widget-legend text-muted")
      .selectAll("li")
      .data(jsArray)
      .enter()
      .append("li")
      .attr("data-slice", (d: MultipleProgressChartItem, i: Int) => i)
      .attr("style", (d: MultipleProgressChartItem, i: Int) => s"border-bottom: solid 2px ${d.color}")
      .text((d: MultipleProgressChartItem, i: Int) => d.name)

    d3.transition().each(update)

    def arcTween(d: MultipleProgressChartItem) = {
      println(d.previousValue + " --- " + d.percentage)

      // TOOD: used to be arc(d)
      val i = d3.interpolateNumber(d.previousValue, d.percentage)
      t: js.Any => {
        d.percentage = i(t).asInstanceOf[Int]
        arc(d.asInstanceOf[js.Object])
      }
    }

    def update: js.Any = {
      field = field
        .each((d: MultipleProgressChartItem) => d.previousValue = d.percentage)
        .data(jsArray)
        .each((d: MultipleProgressChartItem) => {
          d.percentage = Math.round(Math.random() * 100).asInstanceOf[Int] + 1
        })

      field
        .select("path.arc-foreground")
        .transition()
        .duration(750)
        .ease("easeInOut")
        .attrTween("d", (d: MultipleProgressChartItem) => arcTween(d))

      setTimeout(4000){
        update
      }
    }
  }

  def render() =
    div(className := "svg-center", id := elementId)
}

case class MultipleProgressChartItem(name: String, color: String, var percentage: Int, var previousValue: Int)