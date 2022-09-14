package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.{div, ref}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class MenuItem extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(label: String,
                   active: Option[Boolean] = None,
                   checked: Option[Boolean] = None,
                   className: Option[String] = None,
                   disabled: Option[Boolean] = None,
                   iconPrefix: Option[(String, String)] = None,
                   iconSuffix: Option[(String, String)] = None,
                   static: Boolean = false,
                   value: Option[String] = None,
                   onActivate: Option[js.Any => Unit] = None,
                   onClick: Option[js.Any => Unit] = None,
                   onDeactivate: Option[js.Any => Unit] = None)

	override def shouldComponentUpdate(nextProps: Props, nextState: Unit) = {
    val next = super.shouldComponentUpdate(nextProps, nextState)
    if (props.static) false else next
	}

  override def componentDidMount(): Unit = {
    if (props.onActivate.isDefined) elementRef.current.addEventListener("sl-activate", props.onActivate.get)
    if (props.onClick.isDefined && !props.disabled.getOrElse(false)) elementRef.current.addEventListener("click", props.onClick.get)
    if (props.onDeactivate.isDefined) elementRef.current.addEventListener("sl-deactivate", props.onDeactivate.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onActivate.isDefined) elementRef.current.removeEventListener("sl-activate", props.onActivate.get)
    if (props.onClick.isDefined) elementRef.current.removeEventListener("click", props.onClick.get)
    if (props.onDeactivate.isDefined) elementRef.current.removeEventListener("sl-deactivate", props.onDeactivate.get)
  }

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.active, "active")
    add(attrs, Some(s"menu-item ${props.className.getOrElse("")}"), "class")
    add(attrs, props.checked, "checked")
    add(attrs, props.disabled, "disabled")
    add(attrs, props.value, "value")

    attrs += (ref := elementRef)

    val children = new ListBuffer[ReactElement]()
    children += props.label
    if (props.iconPrefix.isDefined) children += icon("prefix", props.className, props.iconPrefix.get)
    if (props.iconSuffix.isDefined) children += icon("suffix", props.className, props.iconSuffix.get)

    CustomTag("sl-menu-item")(attrs.toSeq: _*)(children.toSeq: _*)
  }
}