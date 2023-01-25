package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html._

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Switch extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(checked: Option[Boolean] = None,
                   className: Option[String] = None,
                   disabled: Option[Boolean] = None,
                   height: Option[String] = None,
                   invalid: Option[Boolean] = None,
                   label: Option[ReactElement] = None,
                   name: String,
                   required: Option[Boolean] = None,
                   thumbSize: Option[String] = None,
                   value: Option[String] = None,
                   width: Option[String] = None,
                   onBlur: Option[js.Any => Unit] = None,
                   onChange: Option[Boolean => Unit] = None,
                   onFocus: Option[js.Any => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onBlur.nonEmpty) elementRef.current.addEventListener("sl-blur", props.onBlur.get)
    if (props.onChange.nonEmpty) elementRef.current.addEventListener("sl-change", e => handleChecked(e, props.onChange.get))
    if (props.onFocus.nonEmpty) elementRef.current.addEventListener("sl-focus", props.onFocus.get)
  }

  def blur() =
    elementRef.current.asInstanceOf[js.Dynamic].blur()

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
    add(attrs, props.required, "required")
    add(attrs, props.value, "value")

    attrs += (ref := elementRef)

    val children = new ListBuffer[ReactElement]()
    if (props.label.nonEmpty) children += props.label.get

    val style = js.Dynamic.literal()
    if (props.thumbSize.nonEmpty) style.updateDynamic("--thumb-size")(props.thumbSize.get)
    if (props.height.nonEmpty) style.updateDynamic("--height")(props.height.get)
    if (props.width.nonEmpty) style.updateDynamic("--width")(props.width.get)
    add(attrs, Some(style), "style")


    CustomTag("sl-switch")(attrs.toSeq: _*)(children.toSeq: _*)
  }
}