package com.harana.ui.components.elements

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html.{div, id}

import scala.util.Random

@react class BulletChart extends StatelessComponent {

  val elementId = s"bulletchart-${Random.nextInt(100000)}"

  case class Props(data: List[Int],
                   height: Int,
                   color: String,
                   tooltip: Option[String] = None,
                   animationEasingType: Option[BarChartAnimationEasingType] = None,
                   animationDuration: Option[Int] = None,
                   animationDelay: Option[Int] = None)

//  private def bullet = {
//
//    val orient = "left"
//    val reverse = false
//    val duration = 750
//    val ranges = bulletRanges
//    val markers = bulletMarkers
//    val measures = bulletMeasures
//    val height = 30
//    val tickFormat = null
//
//    val d3 = js.Dynamic.global.d3
//    val d3Container = d3.select(s"#$elementId")
//
//
//    def bullet(g) {
//
//      g.each((d: js.Any, i: js.Any) => {
//
//        val rangez = ranges.call(this, d, i).slice().sort(d3.descending)
//        val markerz = markers.call(this, d, i).slice().sort(d3.descending)
//        val measurez = measures.call(this, d, i).slice().sort(d3.descending)
//        val g = d3.select(this)
//
//        val x1 = d3.scale.linear()
//          .domain(js.Array(0, Math.max(rangez(0), markerz(0), measurez(0))))
//          .range(if (reverse) js.Array(width, 0) else js.Array(0, width))
//
//          // Retrieve the old x-scale, if this is an update.
//          val x0 = this.__chart__ || d3.scale.linear()
//            .domain([0, Infinity])
//          .range(x1.range())
//
//          // Stash the new scale.
//          this.__chart__ = x1
//
//          val w0 = bulletWidth(x0)
//          val w1 = bulletWidth(x1)
//
//          val range = g.selectAll(".bullet-range").data(rangez)
//
//          range.enter()
//            .append("rect")
//            .attr("class", (_: js.Any, i: Int) => { return "bullet-range bullet-range-" + (i + 1) })
//            .attr("width", w0)
//            .attr("height", height)
//            .attr("rx", 2)
//            .attr("x", if (reverse) x0 else 0)
//
//            .transition()
//            .duration(duration)
//            .attr("width", w1)
//            .attr("x", if (reverse) x1 else 0)
//
//          range.transition()
//            .duration(duration)
//            .attr("x", if (reverse) x1 else 0)
//            .attr("width", w1)
//            .attr("height", height)
//
//         val measure = g.selectAll(".bullet-measure")
//            .data(measurez)
//
//          measure.enter()
//            .append("rect")
//            .attr("class", (_: js.Any, i: Int) => s"bullet-measure bullet-measure-${i + 1}")
//            .attr("width", w0)
//            .attr("height", height / 5)
//            .attr("x", if (reverse) x0 else 0)
//            .attr("y", height / 2.5)
//            .style("shape-rendering", "crispEdges")
//
//          measure.transition()
//            .duration(duration)
//            .attr("width", w1)
//            .attr("x", if (reverse) x1 else 0)
//
//          measure.transition()
//            .duration(duration)
//            .attr("width", w1)
//            .attr("height", height / 5)
//            .attr("x", if (reverse) x1 else 0)
//            .attr("y", height / 2.5)
//
//          val marker = g.selectAll(".bullet-marker")
//            .data(markerz)
//
//          marker.enter()
//            .append("line")
//            .attr("class", (_: js.Any, i: Int) => s"bullet-marker bullet-marker-${i + 1}")
//            .attr("x1", x0)
//            .attr("x2", x0)
//            .attr("y1", height / 6)
//            .attr("y2", height * 5 / 6)
//
//          marker.transition()
//            .duration(duration)
//            .attr("x1", x1)
//            .attr("x2", x1)
//
//          marker.transition()
//            .duration(duration)
//            .attr("x1", x1)
//            .attr("x2", x1)
//            .attr("y1", height / 6)
//            .attr("y2", height * 5 / 6)
//
//          val format = tickFormat || x1.tickFormat(8)
//
//          val tick = g.selectAll(".bullet-tick")
//            .data(x1.ticks(8), (d: js.Any) {
//              return this.textContent || format(d)
//            })
//
//          val tickEnter = tick.enter()
//            .append("g")
//            .attr("class", "bullet-tick")
//            .attr("transform", bulletTranslate(x0))
//            .style("opacity", 1e-6)
//
//          tickEnter.append("line")
//            .attr("y1", height)
//            .attr("y2", (height * 7 / 6) + 3)
//
//          tickEnter.append("text")
//            .attr("text-anchor", "middle")
//            .attr("dy", "1em")
//            .attr("y", (height * 7 / 6) + 4)
//            .text(format)
//
//          tickEnter.transition()
//            .duration(duration)
//            .attr("transform", bulletTranslate(x1))
//            .style("opacity", 1)
//
//          val tickUpdate = tick.transition()
//            .duration(duration)
//            .attr("transform", bulletTranslate(x1))
//            .style("opacity", 1)
//
//          tickUpdate.select("line")
//            .attr("y1", height + 3)
//            .attr("y2", (height * 7 / 6) + 3)
//
//          tickUpdate.select("text")
//            .attr("y", (height * 7 / 6) + 4)
//
//          tick.exit()
//            .transition()
//            .duration(duration)
//            .attr("transform", bulletTranslate(x1))
//            .style("opacity", 1e-6)
//            .remove()
//
//          // Call function on window resize
//          $(window).on("resize", resizeBulletsCore)
//
//          // Call function on sidebar width change
//          //$(document).on("click", ".sidebar-control", resizeBulletsCore)
//
//
//          def resize = {
//
//            val width = d3.select("#bullets").node().getBoundingClientRect().width.asInstanceOf[Int] - margin.left - margin.right
//            w1 = bulletWidth(x1)
//
//            x1.range(if (reverse) js.Array(width, 0) else js.Array(0, width))
//
//            g.selectAll(".bullet-measure").attr("width", w1).attr("x", if (reverse) x1 else 0)
//            g.selectAll(".bullet-range").attr("width", w1).attr("x", if (reverse) x1 else 0)
//            g.selectAll(".bullet-marker").attr("x1", x1).attr("x2", x1)
//            g.selectAll(".bullet-tick").attr("transform", bulletTranslate(x1))
//          }
//
//        })
//
//        d3.timer.flush()
//      }
//
//
//      // Constructor functions
//      // ------------------------------
//
//      // Left, right, top, bottom
//      bullet.orient = function(x) {
//        if (!arguments.length) return orient
//        orient = x
//        reverse = orient == "right" || orient == "bottom"
//        return bullet
//      }
//
//      // Ranges (bad, satisfactory, good)
//      bullet.ranges = function(x) {
//        if (!arguments.length) return ranges
//        ranges = x
//        return bullet
//      }
//
//      // Markers (previous, goal)
//      bullet.markers = function(x) {
//        if (!arguments.length) return markers
//        markers = x
//        return bullet
//      }
//
//      // Measures (actual, forecast)
//      bullet.measures = function(x) {
//        if (!arguments.length) return measures
//        measures = x
//        return bullet
//      }
//
//      // Width
//      bullet.width = function(x) {
//        if (!arguments.length) return width
//        width = x
//        return bullet
//      }
//
//      // Height
//      bullet.height = function(x) {
//        if (!arguments.length) return height
//        height = x
//        return bullet
//      }
//
//      // Axex tick format
//      bullet.tickFormat = function(x) {
//        if (!arguments.length) return tickFormat
//        tickFormat = x
//        return bullet
//      }
//
//      // Transition duration
//      bullet.duration = function(x) {
//        if (!arguments.length) return duration
//        duration = x
//        return bullet
//      }
//
//      return bullet
//    }
//
//    private def bulletRanges(d: js.Any) = d.ranges
//    private def bulletMarkers(d: js.Any) = d.markers
//    private def bulletMeasures(d: js.Any) = d.measures
//    private def bulletTranslate(x: js.Any) = (d: js.Any) => s"translate(${x(d)},0)"
//    private def bulletWidth(x: js.Any) = (d: js.Any) => Math.abs(x(d) - x(0))
//
//  override def componentDidMount(): Unit = {
//    window.onload = { _ =>
//      val jsArray = props.data.toJSArray
//      val d3 = js.Dynamic.global.d3
//      val d3Container = d3.select(s"#$elementId")
//      val width = d3Container.node().getBoundingClientRect().width
//
//      val delay = props.animationDelay.getOrElse(50)
//      val duration = props.animationDuration.getOrElse(1200)
//      val easing = props.animationEasingType.getOrElse(BarChartAnimationEasingType.Elastic).value
//
//      val x = d3.scale.ordinal().rangeBands(js.Array(0, width), 0.3)
//      val y = d3.scale.linear().range(js.Array(0, props.height))
//
//      x.domain(d3.range(0, jsArray.length))
//      y.domain(js.Array(0, d3.max(jsArray)))
//
//      val container = d3Container.append("svg")
//      val svg = container.attr("width", width).attr("height", props.height).append("g")
//
//      svg.selectAll("rect")
//        .data(jsArray)
//        .enter()
//        .append("rect")
//        .attr("class", "d3-random-bars")
//        .attr("width", x.rangeBand())
//        .attr("x", (_: Int, i: Int) => x(i))
//        .style("fill", props.color)
//        .attr("height", 0)
//        .attr("y", props.height)
//        .transition()
//        .attr("height", (d: Int) => y(d))
//        .attr("y", (d: Int) => props.height - y(d).asInstanceOf[Double])
//        .delay((_: Int, i: Int) => i * delay)
//        .duration(duration)
//        .ease(easing)
//
//      def resize = {
//        val width = d3Container.node().getBoundingClientRect().width
//        container.attr("width", width)
//        svg.attr("width", width)
//        x.rangeBands(js.Array(0, width), 0.3)
//
//        svg.selectAll(".d3-random-bars")
//          .attr("width", x.rangeBand())
//          .attr("x", (_: Int, i: Int) => x(i))
//      }
//
//      window.onresize = (_: UIEvent) => resize
//      //document.on("click", ".sidebar-control", resizeBarChart(d3Container, container, svg, x))
//    }
//  }

  def render() =
    div(id := elementId)
}