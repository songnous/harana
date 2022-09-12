package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.React
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Range extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(className: Option[String] = None,
                   disabled: Option[Boolean] = None,
                   helpText: Option[String] = None,
                   invalid: Option[Boolean] = None,
                   max: Option[Double] = None,
                   min: Option[Double] = None,
                   name: String,
                   step: Option[Double] = None,
                   thumbSize: Option[Int] = None,
                   tooltip: Option[String] = None,
                   tooltipFormatter: Option[Double => String] = None,
                   tooltipOffset: Option[Int] = None,
                   trackColorActive: Option[String] = None,
                   trackColorInactive: Option[String] = None,
                   trackHeight: Option[String] = None,
                   value: Option[String] = None,
                   onBlur: Option[js.Any => Unit] = None,
                   onChange: Option[js.Function1[Double, Unit]] = None,
                   onFocus: Option[js.Any => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.addEventListener("sl-blur", props.onBlur.get)
    if (props.onChange.isDefined) elementRef.current.addEventListener("sl-change", e => handleValue[Double](e, props.onChange.get))
    if (props.onFocus.isDefined) elementRef.current.addEventListener("sl-focus", props.onFocus.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onBlur.isDefined) elementRef.current.removeEventListener("sl-blur", props.onBlur.get)
    if (props.onChange.isDefined) elementRef.current.removeEventListener("sl-change", e => handleValue[Double](e, props.onChange.get))
    if (props.onFocus.isDefined) elementRef.current.removeEventListener("sl-focus", props.onFocus.get)
  }

  def blur() =
    elementRef.current.asInstanceOf[js.Dynamic].blur()

  def focus() =
    elementRef.current.asInstanceOf[js.Dynamic].focus()

  def setCustomValidity() =
    elementRef.current.asInstanceOf[js.Dynamic].setCustomValidity()

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.disabled, "disabled")
    add(attrs, props.helpText, "help-text")
    add(attrs, props.invalid, "invalid")
    add(attrs, props.max, "max")
    add(attrs, props.min, "min")
    add(attrs, props.name, "name")
    add(attrs, props.step, "step")
    add(attrs, props.tooltip, "tooltip")
    add(attrs, props.tooltipFormatter, "tooltipFormatter")
    add(attrs, props.value, "value")

    attrs += (ref := elementRef)

    val style = js.Dynamic.literal()
    if (props.thumbSize.isDefined) style.updateDynamic("--thumb-size")(props.thumbSize.get)
    if (props.tooltipOffset.isDefined) style.updateDynamic("--tooltip-offset")(props.tooltipOffset.get)
    if (props.trackColorActive.isDefined) style.updateDynamic("--track-color-active")(props.trackColorActive.get)
    if (props.trackColorInactive.isDefined) style.updateDynamic("--track-color-inactive")(props.trackColorInactive.get)
    if (props.trackHeight.isDefined) style.updateDynamic("--track-height")(props.trackHeight.get)
    add(attrs, Some(style), "style")

    CustomTag("sl-range")(attrs: _*)
  }
}