package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class TabGroup extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(children: List[ReactElement] = List(),
                   activation: Option[String] = None,
                   className: Option[String] = None,
                   indicatorColor: Option[String] = None,
                   lang: Option[String] = None,
                   noScrollControls: Option[Boolean] = None,
                   placement: Option[String] = None,
                   trackColor: Option[String] = None,
                   onTabHide: Option[js.Any => Unit] = None,
                   onTabShow: Option[js.Any => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onTabHide.isDefined) elementRef.current.addEventListener("sl-tab-hide", props.onTabHide.get)
    if (props.onTabShow.isDefined) elementRef.current.addEventListener("sl-tab-show", props.onTabShow.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onTabHide.isDefined) elementRef.current.removeEventListener("sl-tab-hide", props.onTabHide.get)
    if (props.onTabShow.isDefined) elementRef.current.removeEventListener("sl-tab-show", props.onTabShow.get)
  }

  def show() =
    elementRef.current.asInstanceOf[js.Dynamic].show()

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.activation, "activation")
    add(attrs, props.className, "class")
    add(attrs, props.lang, "lang")
    add(attrs, props.noScrollControls, "no-scroll-controls")
    add(attrs, props.placement, "placement")

    attrs += (ref := elementRef)

    val style = js.Dynamic.literal()
    if (props.indicatorColor.isDefined) style.updateDynamic("--indicator-color")(props.indicatorColor.get)
    if (props.trackColor.isDefined) style.updateDynamic("--track-color")(props.trackColor.get)
    add(attrs, Some(style), "style")

    CustomTag("sl-tab-group")(attrs: _*)(props.children: _*)
  }
}