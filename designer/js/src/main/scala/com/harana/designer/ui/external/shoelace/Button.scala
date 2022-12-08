package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Button extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(badge: Option[Badge.Props] = None,
                   caret: Option[Boolean] = None,
                   circle: Option[Boolean] = None,
                   className: Option[String] = None,
                   content: Option[ReactElement] = None,
                   disabled: Option[Boolean] = None,
                   download: Option[String] = None,
                   href: Option[String]= None,
                   icon: Option[(String, String)] = None,
                   iconClassName: Option[String] = None,
                   iconPrefix: Option[(String, String)] = None,
                   iconSuffix: Option[(String, String)] = None,
                   label: Option[String] = None,
                   loading: Option[Boolean] = None,
                   outline: Option[Boolean] = None,
                   pill: Option[Boolean] = None,
                   size: Option[String] = None,
                   slot: Option[String] = None,
                   target: Option[String] = None,
                   tooltip: Option[String] = None,
                   `type`: Option[String] = None,
                   value: Option[String] = None,
                   variant: Option[String] = None,
                   onBlur: Option[js.Any => Unit] = None,
                   onClick: Option[js.Any => Unit] = None,
                   onFocus: Option[js.Any => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.addEventListener("sl-blur", props.onBlur.get)
    if (props.onClick.isDefined) elementRef.current.addEventListener("click", props.onClick.get)
    if (props.onFocus.isDefined) elementRef.current.addEventListener("sl-focus", props.onFocus.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.removeEventListener("sl-blur", props.onBlur.get)
    if (props.onClick.isDefined) elementRef.current.removeEventListener("click", props.onClick.get)
    if (props.onFocus.isDefined) elementRef.current.removeEventListener("sl-focus", props.onFocus.get)
  }

  def blur() =
    elementRef.current.asInstanceOf[js.Dynamic].blur()

  def focus() =
    elementRef.current.asInstanceOf[js.Dynamic].focus()

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.caret, "caret")
    add(attrs, props.circle, "circle")
    add(attrs, props.className, "class")
    add(attrs, props.disabled, "disabled")
    add(attrs, props.download, "download")
    add(attrs, props.href, "href")
    add(attrs, props.loading, "loading")
    add(attrs, props.outline, "outline")
    add(attrs, props.pill, "pill")
    add(attrs, props.size, "size")
    add(attrs, props.slot, "slot")
    add(attrs, props.target, "target")
    add(attrs, props.`type`, "type")
    add(attrs, props.value, "value")
    add(attrs, props.variant, "variant")

    attrs += (ref := elementRef)

    val children = new ListBuffer[ReactElement]()
    if (props.iconPrefix.isDefined) children += icon("prefix", props.className, props.iconPrefix.get)
    if (props.iconSuffix.isDefined) children += icon("suffix", props.className, props.iconSuffix.get)
    if (props.icon.isDefined) children += icon("", props.iconClassName, props.icon.get)
    if (props.content.isDefined) children += props.content.get
    if (props.label.isDefined) children += props.label.get
    if (props.badge.isDefined) children += Badge(props.badge.get)

    val button = CustomTag("sl-button")(attrs.toSeq: _*)(children.toSeq: _*)
    if (props.tooltip.isDefined) Tooltip(props.tooltip.get)(List(button)) else button
  }
}