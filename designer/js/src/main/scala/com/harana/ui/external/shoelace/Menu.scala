package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Menu extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(className: Option[String] = None,
                   items: List[ReactElement] = List(),
                   onBlur: Option[js.Any => Unit] = None,
                   onFocus: Option[js.Any => Unit] = None,
                   onSelect: Option[String => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.addEventListener("sl-blur", props.onBlur.get)
    if (props.onFocus.isDefined) elementRef.current.addEventListener("sl-focus", props.onFocus.get)
    if (props.onSelect.isDefined) elementRef.current.addEventListener("sl-select", props.onSelect.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.removeEventListener("sl-blur", props.onBlur.get)
    if (props.onFocus.isDefined) elementRef.current.removeEventListener("sl-focus", props.onFocus.get)
    if (props.onSelect.isDefined) elementRef.current.removeEventListener("sl-select", props.onSelect.get)
  }

  def blur() =
    elementRef.current.asInstanceOf[js.Dynamic].blur()

  def focus() =
    elementRef.current.asInstanceOf[js.Dynamic].focus()

  def typeToSelect() =
    elementRef.current.asInstanceOf[js.Dynamic].reportValidity()

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    attrs += (ref := elementRef)
    add(attrs, Some(s"menu ${props.className.getOrElse("")}"), "class")

    CustomTag("sl-menu")(attrs: _*)(props.items)
  }
}