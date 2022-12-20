package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Tooltip extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(content: String,
                   children: List[ReactElement] = List(),
                   className: Option[String] = None,
                   disabled: Option[Boolean] = None,
                   distance: Option[Int] = None,
                   hideDelay: Option[Double] = None,
                   hoist: Option[Boolean] = None,
                   maxWidth: Option[Int] = None,
                   open: Option[Boolean] = None,
                   placement: Option[String] = None,
                   showDelay: Option[Double] = None,
                   skidding: Option[String] = None,
                   slot: Option[String] = None,
                   trigger: Option[String] = None,
                   onAfterHide: Option[js.Any => Unit] = None,
                   onAfterShow: Option[js.Any => Unit] = None,
                   onHide: Option[js.Any => Unit] = None,
                   onShow: Option[js.Any => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onAfterHide.isDefined) elementRef.current.addEventListener("sl-after-hide", props.onAfterHide.get)
    if (props.onAfterShow.isDefined) elementRef.current.addEventListener("sl-after-show", props.onAfterShow.get)
    if (props.onHide.isDefined) elementRef.current.addEventListener("sl-hide", props.onHide.get)
    if (props.onShow.isDefined) elementRef.current.addEventListener("sl-show", props.onShow.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onAfterHide.isDefined) elementRef.current.removeEventListener("sl-after-hide", props.onAfterHide.get)
    if (props.onAfterShow.isDefined) elementRef.current.removeEventListener("sl-after-show", props.onAfterShow.get)
    if (props.onHide.isDefined) elementRef.current.removeEventListener("sl-hide", props.onHide.get)
    if (props.onShow.isDefined) elementRef.current.removeEventListener("sl-show", props.onShow.get)
  }

  def show() =
    elementRef.current.asInstanceOf[js.Dynamic].show()

  def hide() =
    elementRef.current.asInstanceOf[js.Dynamic].hide()

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, Some(props.content), "content")
    add(attrs, props.className, "class")
    add(attrs, props.disabled, "disabled")
    add(attrs, props.hoist, "hoist")
    add(attrs, props.open, "open")
    add(attrs, props.placement, "placement")
    add(attrs, props.skidding, "skidding")
    add(attrs, props.slot, "slot")
    add(attrs, props.trigger, "trigger")

    attrs += (ref := elementRef)

    val style = js.Dynamic.literal()
    if (props.hideDelay.isDefined) style.updateDynamic("--hide-delay")(props.hideDelay.get)
    if (props.maxWidth.isDefined) style.updateDynamic("--max-width")(props.maxWidth.get)
    if (props.showDelay.isDefined) style.updateDynamic("--show-delay")(props.showDelay.get)
    add(attrs, Some(style), "style")

    CustomTag("sl-tooltip")(attrs.toSeq: _*)(props.children: _*)
  }
}