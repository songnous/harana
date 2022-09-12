package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Alert extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(children: List[ReactElement],
                   className: Option[String] = None,
                   closable: Option[Boolean] = None,
                   duration: Option[Long] = None,
                   icon: Option[(String, String)] = None,
                   open: Option[Boolean] = None,
                   slot: Option[String] = None,
                   `type`: Option[String] = None,
                   variant: Option[String] = None,
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

  def toast() =
    elementRef.current.asInstanceOf[js.Dynamic].toast()

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.closable, "closable")
    add(attrs, props.duration, "duration")
    add(attrs, props.open, "open")
    add(attrs, props.open, "slot")
    add(attrs, props.`type`, "type")
    add(attrs, props.variant, "variant")

    attrs += (ref := elementRef)

    val children = new ListBuffer[ReactElement]()
    if (props.icon.isDefined) children += icon("icon", props.className, props.icon.get)
    children += props.children

    CustomTag("sl-alert")(attrs: _*)(children: _*)
  }
}