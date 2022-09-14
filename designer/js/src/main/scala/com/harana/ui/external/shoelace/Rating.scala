package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.React
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Rating extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(className: Option[String] = None,
                   disabled: Option[Boolean] = None,
                   getSymbol: Option[Option[Int] => String] = None,
                   max: Option[Int] = None,
                   precision: Option[Int] = None,
                   readonly: Option[Boolean] = None,
                   symbolColor: Option[String] = None,
                   symbolColorActive: Option[String] = None,
                   symbolSize: Option[Int] = None,
                   symbolSpacing: Option[Int] = None,
                   value: Option[Int] = None,
                   onChange: Option[js.Function1[Int, Unit]] = None)

  override def componentDidMount(): Unit = {
    if (props.onChange.isDefined) elementRef.current.addEventListener("sl-change", e => handleValue[Int](e, props.onChange.get))
  }

  override def componentWillUnmount(): Unit = {
    if (props.onChange.isDefined) elementRef.current.removeEventListener("sl-change", e => handleValue[Int](e, props.onChange.get))
  }

  def blur() =
    elementRef.current.asInstanceOf[js.Dynamic].blur()

  def focus() =
    elementRef.current.asInstanceOf[js.Dynamic].focus()

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.disabled, "disabled")
    add(attrs, props.getSymbol, "getSymbol")
    add(attrs, props.max, "max")
    add(attrs, props.precision, "precision")
    add(attrs, props.readonly, "readonly")
    add(attrs, props.value, "value")

    attrs += (ref := elementRef)

    val style = js.Dynamic.literal()
    if (props.symbolColor.isDefined) style.updateDynamic("--symbol-color")(props.symbolColor.get)
    if (props.symbolColorActive.isDefined) style.updateDynamic("--symbol-color-active")(props.symbolColorActive.get)
    if (props.symbolSize.isDefined) style.updateDynamic("--symbol-size")(props.symbolSize.get)
    if (props.symbolSpacing.isDefined) style.updateDynamic("--symbol-spacing")(props.symbolSpacing.get)
    add(attrs, Some(style), "style")

    CustomTag("sl-rating")(attrs.toSeq: _*)
  }
}