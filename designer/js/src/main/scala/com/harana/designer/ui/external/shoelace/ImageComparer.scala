package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.React
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class ImageComparer extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(dividerWidth: Option[String] = None,
                   handleSize: Option[String] = None,
                   position: Option[Int] = None,
                   onChange: Option[String => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onChange.isDefined) elementRef.current.addEventListener("sl-change", props.onChange.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onChange.isDefined) elementRef.current.removeEventListener("sl-change", props.onChange.get)
  }

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.position, "position")

    attrs += (ref := elementRef)

    val style = js.Dynamic.literal()
    if (props.dividerWidth.isDefined) style.updateDynamic("--divider-width")(props.dividerWidth.get)
    if (props.handleSize.isDefined) style.updateDynamic("--handle-size")(props.handleSize.get)
    add(attrs, Some(style), "style")

    CustomTag("sl-image-comparer")(attrs.toSeq: _*)
  }
}