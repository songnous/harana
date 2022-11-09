package com.harana.ui.external.shoelace

import org.scalajs.dom.{Event, HTMLElement, HTMLInputElement, KeyboardEvent}
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.{div, ref}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Input extends StatelessComponent {

  val elementRef = React.createRef[HTMLInputElement]

  case class Props(autoCapitalize: Option[String] = None,
                   autoComplete: Option[String] = None,
                   autoCorrect: Option[String] = None,
                   autoFocus: Option[Boolean] = None,
                   borderColor: Option[String] = None,
                   className: Option[String] = None,
                   clearable: Option[Boolean] = None,
                   disabled: Option[Boolean] = None,
                   helpText: Option[String] = None,
                   iconPrefix: Option[(String, String)] = None,
                   iconSuffix: Option[(String, String)] = None,
                   inputMode: Option[String] = None,
                   invalid: Option[Boolean] = None,
                   label: Option[String] = None,
                   length: Option[Int] = None,
                   max: Option[Int] = None,
                   maxLength: Option[Int] = None,
                   min: Option[Int] = None,
                   minLength: Option[Int] = None,
                   name: String,
                   numbersOnly: Option[Boolean] = None,
                   pattern: Option[String] = None,
                   pill: Option[Boolean] = None,
                   placeholder: Option[String] = None,
                   readonly: Option[Boolean] = None,
                   required: Option[Boolean] = None,
                   size: Option[String] = None,
                   spellCheck: Option[Boolean] = None,
                   step: Option[Int] = None,
                   togglePassword: Option[Boolean] = None,
                   `type`: Option[String] = None,
                   value: Option[String] = None,
                   onBlur: Option[js.Any => Unit] = None,
                   onChange: Option[String => Unit] = None,
                   onClear: Option[js.Any => Unit] = None,
                   onFocus: Option[js.Any => Unit] = None,
                   onInput: Option[String => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.addEventListener("sl-blur", props.onBlur.get)
    if (props.onChange.isDefined) elementRef.current.addEventListener("sl-change", e => handleValue[String](e, props.onChange.get))
    if (props.onClear.isDefined) elementRef.current.addEventListener("sl-clear", props.onClear.get)
    if (props.onFocus.isDefined) elementRef.current.addEventListener("sl-focus", props.onFocus.get)
    if (props.onInput.isDefined) elementRef.current.addEventListener("sl-input", e => handleValue[String](e, props.onInput.get))

    elementRef.current.addEventListener("keydown", (e: KeyboardEvent) => {
      val allowed = List(8, 13, 16, 37, 38, 39, 40)

      if (props.numbersOnly.getOrElse(false)) {
        if (!allowed.contains(e.keyCode) && (e.keyCode < 48 || e.keyCode > 57)) e.preventDefault()
      }

      if (props.maxLength.isDefined && props.`type`.getOrElse("") == "number") {
        if (elementRef.current.value.length >= props.maxLength.get && !allowed.contains(e.keyCode)) e.preventDefault()
      }
    })

  }

  override def componentWillUnmount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.removeEventListener("sl-blur", props.onBlur.get)
    if (props.onChange.isDefined) elementRef.current.removeEventListener("sl-change", e => handleValue[String](e, props.onChange.get))
    if (props.onClear.isDefined) elementRef.current.removeEventListener("sl-clear", props.onClear.get)
    if (props.onFocus.isDefined) elementRef.current.removeEventListener("sl-focus", props.onFocus.get)
    if (props.onInput.isDefined) elementRef.current.removeEventListener("sl-input", e => handleValue[String](e, props.onInput.get))
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
    add(attrs, props.clearable, "clearable")
    add(attrs, props.disabled, "disabled")
    add(attrs, props.inputMode, "inputmode")
    add(attrs, props.invalid, "invalid")
    add(attrs, props.label, "label")
    add(attrs, props.max, "max")
    add(attrs, props.maxLength, "maxlength")
    add(attrs, props.min, "min")
    add(attrs, props.minLength, "minlength")
    add(attrs, props.name, "name")
    add(attrs, props.pattern, "pattern")
    add(attrs, props.pill, "pill")
    add(attrs, props.placeholder, "placeholder")
    add(attrs, props.readonly, "readonly")
    add(attrs, props.required, "required")
    add(attrs, props.size, "size")
    add(attrs, props.spellCheck, "spellcheck")
    add(attrs, props.step, "step")
    add(attrs, props.togglePassword, "toggle-password")
    add(attrs, props.`type`, "type")
    add(attrs, props.value.getOrElse(""), "value")

    attrs += (ref := elementRef)

    val style = js.Dynamic.literal()
    if (props.borderColor.isDefined) style.updateDynamic("--sl-input-border-color")(props.borderColor.get)
    if (props.length.isDefined) style.updateDynamic("width")(s"${props.length.get * 2.5}ch")
    add(attrs, Some(style), "style")

    val children = new ListBuffer[ReactElement]()
    if (props.helpText.isDefined) children += div(slotAttr := "help-text")(props.helpText.get)
    if (props.iconPrefix.isDefined) children += icon("prefix", props.className, props.iconPrefix.get)
    if (props.iconSuffix.isDefined) children += icon("suffix", props.className, props.iconSuffix.get)

    CustomTag("sl-input")(attrs.toSeq: _*)(children.toSeq)
  }
}