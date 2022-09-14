package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Radio extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(checked: Option[Boolean] = None,
                   className: Option[String] = None,
                   disabled: Option[Boolean] = None,
                   invalid: Option[Boolean] = None,
                   label: Option[String] = None,
                   name: String,
                   value: Option[String] = None,
                   onBlur: Option[js.Any => Unit] = None,
                   onChange: Option[Boolean => Unit] = None,
                   onFocus: Option[js.Any => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.addEventListener("sl-blur", props.onBlur.get)
    if (props.onChange.isDefined) elementRef.current.addEventListener("sl-change", e => handleChecked(e, props.onChange.get))
    if (props.onFocus.isDefined) elementRef.current.addEventListener("sl-focus", props.onFocus.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.removeEventListener("sl-blur", props.onBlur.get)
    if (props.onChange.isDefined) elementRef.current.removeEventListener("sl-change", e => handleChecked(e, props.onChange.get))
    if (props.onFocus.isDefined) elementRef.current.removeEventListener("sl-focus", props.onFocus.get)
  }

  def blur() =
    elementRef.current.asInstanceOf[js.Dynamic].blur()

  def click() =
    elementRef.current.asInstanceOf[js.Dynamic].click()

  def focus() =
    elementRef.current.asInstanceOf[js.Dynamic].focus()

  def reportValidity() =
    elementRef.current.asInstanceOf[js.Dynamic].reportValidity()

  def setCustomValidity() =
    elementRef.current.asInstanceOf[js.Dynamic].setCustomValidity()

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.checked, "checked")
    add(attrs, props.className, "class")
    add(attrs, props.disabled, "disabled")
    add(attrs, props.invalid, "invalid")
    add(attrs, props.name, "name")
    add(attrs, props.value, "value")

    attrs += (ref := elementRef)

    val children = new ListBuffer[ReactElement]()
    if (props.label.isDefined) children += props.label.get

    CustomTag("sl-radio")(attrs.toSeq: _*)(children.toSeq: _*)
  }
}