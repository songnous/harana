package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.{div, ref}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Select extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(borderColor: Option[String] = None,
                   className: Option[String] = None,
                   clearable: Option[Boolean] = None,
                   disabled: Option[Boolean] = None,
                   filled: Option[Boolean] = None,
                   helpText: Option[String] = None,
                   hoist: Option[Boolean] = None,
                   invalid: Option[Boolean] = None,
                   label: Option[String] = None,
                   maxTagsVisible: Option[Int] = None,
                   multiple: Option[Boolean] = None,
                   name: String,
                   options: List[ReactElement] = List(),
                   pill: Option[Boolean] = None,
                   placeholder: Option[String] = None,
                   required: Option[Boolean] = None,
                   size: Option[String] = None,
                   value: Option[String] = None,
                   onBlur: Option[js.Any => Unit] = None,
                   onChange: Option[String => Unit] = None,
                   onClear: Option[js.Any => Unit] = None,
                   onFocus: Option[js.Any => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.addEventListener("sl-blur", props.onBlur.get)
    if (props.onChange.isDefined) elementRef.current.addEventListener("sl-change", e => handleValue[String](e, props.onChange.get))
    if (props.onClear.isDefined) elementRef.current.addEventListener("sl-clear", props.onClear.get)
    if (props.onFocus.isDefined) elementRef.current.addEventListener("sl-focus", props.onFocus.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.removeEventListener("sl-blur", props.onBlur.get)
    if (props.onChange.isDefined) elementRef.current.removeEventListener("sl-change", e => handleValue[String](e, props.onChange.get))
    if (props.onClear.isDefined) elementRef.current.removeEventListener("sl-clear", props.onClear.get)
    if (props.onFocus.isDefined) elementRef.current.removeEventListener("sl-focus", props.onFocus.get)
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
    add(attrs, props.className, "class")
    add(attrs, props.clearable, "clearable")
    add(attrs, props.disabled, "disabled")
    add(attrs, props.filled, "filled")
    add(attrs, props.helpText, "help-text")
    add(attrs, props.hoist, "hoist")
    add(attrs, props.invalid, "invalid")
    add(attrs, props.label, "label")
    add(attrs, props.maxTagsVisible, "max-tags-visible")
    add(attrs, props.multiple, "multiple")
    add(attrs, props.name, "name")
    add(attrs, props.pill, "pill")
    add(attrs, props.placeholder, "placeholder")
    add(attrs, props.required, "required")
    add(attrs, props.size, "size")
    add(attrs, props.value.getOrElse(""), "value")

    attrs += (ref := elementRef)

    val style = js.Dynamic.literal()
    if (props.borderColor.isDefined) style.updateDynamic("--sl-input-border-color")(props.borderColor.get)
    add(attrs, Some(style), "style")

    val children = new ListBuffer[ReactElement]()
    if (props.helpText.isDefined) children += div(slotAttr := "help-text")(props.helpText.get)
    props.options.foreach(children += _)

    CustomTag("sl-select")(attrs: _*)(children: _*)
  }
}