package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.{div, ref}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class TextArea extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(autoCapitalize: Option[String] = None,
                   autoComplete: Option[String] = None,
                   autoCorrect: Option[String] = None,
                   autoFocus: Option[Boolean] = None,
                   borderColor: Option[String] = None,
                   className: Option[String] = None,
                   disabled: Option[Boolean] = None,
                   filled: Option[Boolean] = None,
                   helpText: Option[String] = None,
                   inputMode: Option[String] = None,
                   invalid: Option[Boolean] = None,
                   label: Option[String] = None,
                   maxLength: Option[Int] = None,
                   minLength: Option[Int] = None,
                   name: String,
                   pattern: Option[String] = None,
                   placeholder: Option[String] = None,
                   readonly: Option[Boolean] = None,
                   required: Option[Boolean] = None,
                   resize: Option[String] = None,
                   rows: Option[Int] = None,
                   size: Option[String] = None,
                   spellCheck: Option[Boolean] = None,
                   value: Option[String] = None,
                   onBlur: Option[js.Any => Unit] = None,
                   onChange: Option[String => Unit] = None,
                   onFocus: Option[js.Any => Unit] = None,
                   onInput: Option[js.Any => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.addEventListener("sl-blur", props.onBlur.get)
    if (props.onChange.isDefined) elementRef.current.addEventListener("sl-change", e => handleValue[String](e, props.onChange.get))
    if (props.onFocus.isDefined) elementRef.current.addEventListener("sl-focus", props.onFocus.get)
    if (props.onInput.isDefined) elementRef.current.addEventListener("sl-input", e => handleValue[String](e, props.onChange.get))
  }

  override def componentWillUnmount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.removeEventListener("sl-blur", props.onBlur.get)
    if (props.onChange.isDefined) elementRef.current.removeEventListener("sl-change", e => handleValue[String](e, props.onChange.get))
    if (props.onFocus.isDefined) elementRef.current.removeEventListener("sl-focus", props.onFocus.get)
    if (props.onInput.isDefined) elementRef.current.removeEventListener("sl-input", e => handleValue[String](e, props.onChange.get))
  }

  def blur() =
    elementRef.current.asInstanceOf[js.Dynamic].blur()

  def focus() =
    elementRef.current.asInstanceOf[js.Dynamic].focus()

  def reportValidity() =
    elementRef.current.asInstanceOf[js.Dynamic].reportValidity()

  def setCustomValidity() =
    elementRef.current.asInstanceOf[js.Dynamic].setCustomValidity()

  def select() =
    elementRef.current.asInstanceOf[js.Dynamic].select()

  def setRangeText() =
    elementRef.current.asInstanceOf[js.Dynamic].setRangeText()

  def setSelectionRange() =
    elementRef.current.asInstanceOf[js.Dynamic].setSelectionRange()

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.autoCapitalize, "autocapitalize")
    add(attrs, props.autoComplete, "autocomplete")
    add(attrs, props.autoCorrect, "autocorrect")
    add(attrs, props.autoFocus, "autofocus")
    add(attrs, props.className, "class")
    add(attrs, props.disabled, "disabled")
    add(attrs, props.filled, "filled")
    add(attrs, props.helpText, "help-text")
    add(attrs, props.inputMode, "inputmode")
    add(attrs, props.invalid, "invalid")
    add(attrs, props.label, "label")
    add(attrs, props.maxLength, "maxlength")
    add(attrs, props.minLength, "minlength")
    add(attrs, props.name, "name")
    add(attrs, props.pattern, "pattern")
    add(attrs, props.placeholder, "placeholder")
    add(attrs, props.readonly, "readonly")
    add(attrs, props.required, "required")
    add(attrs, props.resize, "resize")
    add(attrs, props.size, "size")
    add(attrs, props.spellCheck, "spellcheck")
    add(attrs, props.value.getOrElse(""), "value")

    attrs += (ref := elementRef)

    val style = js.Dynamic.literal()
    if (props.borderColor.isDefined) style.updateDynamic("--sl-input-border-color")(props.borderColor.get)
    add(attrs, Some(style), "style")

    val children = new ListBuffer[ReactElement]()
    if (props.helpText.isDefined) children += div(slotAttr := "help-text")(props.helpText.get)

    CustomTag("sl-textarea")(attrs: _*)(children: _*)
  }
}