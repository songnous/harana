package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.React
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class IconButton extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(className: Option[String] = None,
                   disabled: Option[Boolean] = None,
                   download: Option[String] = None,
                   href: Option[String] = None,
                   label: Option[String] = None,
                   library: Option[String] = None,
                   name: String,
                   src: Option[String] = None,
                   target: Option[String] = None,
                   onClick: Option[js.Any => Unit] = None,
                   tooltip: Option[String] = None)

  override def componentDidMount(): Unit = {
    if (props.onClick.nonEmpty) elementRef.current.addEventListener("click", props.onClick.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onClick.nonEmpty) elementRef.current.removeEventListener("click", props.onClick.get)
  }

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.disabled, "disabled")
    add(attrs, props.download, "download")
    add(attrs, props.href, "href")
    add(attrs, props.label, "label")
    add(attrs, props.library, "library")
    add(attrs, props.name, "name")
    add(attrs, props.src, "src")
    add(attrs, props.target, "target")

    attrs += (ref := elementRef)

    val iconButton = CustomTag("sl-icon-button")(attrs.toSeq: _*)
    if (props.tooltip.nonEmpty) Tooltip(props.tooltip.get)(List(iconButton)) else iconButton
  }
}