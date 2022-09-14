package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.React
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class ColorPicker extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(className: Option[String] = None,
                   disabled: Option[Boolean] = None,
                   format: Option[String] = None,
                   hoist: Option[Boolean] = None,
                   inline: Option[Boolean] = None,
                   invalid: Option[Boolean] = None,
                   lang: Option[String] = None,
                   name: String,
                   noFormatToggle: Option[Boolean] = None,
                   opacity: Option[Boolean] = None,
                   size: Option[String] = None,
                   slot: Option[String] = None,
                   swatches: Option[List[String]] = None,
                   uppercase: Option[Boolean] = None,
                   value: Option[String] = None,
                   onAfterHide: Option[js.Any => Unit] = None,
                   onAfterShow: Option[js.Any => Unit] = None,
                   onChange: Option[String => Unit] = None,
                   onHide: Option[js.Any => Unit] = None,
                   onShow: Option[js.Any => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onAfterHide.isDefined) elementRef.current.addEventListener("sl-after-hide", props.onAfterHide.get)
    if (props.onAfterShow.isDefined) elementRef.current.addEventListener("sl-after-show", props.onAfterShow.get)
    if (props.onChange.isDefined) elementRef.current.addEventListener("sl-change", e => handleValue[String](e, props.onChange.get))
    if (props.onHide.isDefined) elementRef.current.addEventListener("sl-hide", props.onHide.get)
    if (props.onShow.isDefined) elementRef.current.addEventListener("sl-show", props.onShow.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onAfterHide.isDefined) elementRef.current.removeEventListener("sl-after-hide", props.onAfterHide.get)
    if (props.onAfterShow.isDefined) elementRef.current.removeEventListener("sl-after-show", props.onAfterShow.get)
    if (props.onChange.isDefined) elementRef.current.removeEventListener("sl-change", e => handleValue[String](e, props.onChange.get))
    if (props.onHide.isDefined) elementRef.current.removeEventListener("sl-hide", props.onHide.get)
    if (props.onShow.isDefined) elementRef.current.removeEventListener("sl-show", props.onShow.get)
  }

  def getFormattedValue() =
    elementRef.current.asInstanceOf[js.Dynamic].getFormattedValue()

  def reportValidity() =
    elementRef.current.asInstanceOf[js.Dynamic].reportValidity()

  def setCustomValidity() =
    elementRef.current.asInstanceOf[js.Dynamic].setCustomValidity()

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.disabled, "disabled")
    add(attrs, props.format, "format")
    add(attrs, props.hoist, "hoist")
    add(attrs, props.inline, "inline")
    add(attrs, props.invalid, "invalid")
    add(attrs, props.lang, "lang")
    add(attrs, props.name, "name")
    add(attrs, props.noFormatToggle, "no-format-toggle")
    add(attrs, props.opacity, "opacity")
    add(attrs, props.size, "size")
    add(attrs, props.slot, "slot")
    add(attrs, props.swatches, "swatches")
    add(attrs, props.uppercase, "uppercase")
    add(attrs, props.value, "value")

    attrs += (ref := elementRef)

    CustomTag("sl-color-picker")(attrs.toSeq: _*)
  }
}