package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Tag extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(label: String,
                   clearable: Option[Boolean] = None,
                   className: Option[String] = None,
                   pill: Option[Boolean] = None,
                   removable: Option[Boolean] = None,
                   size: Option[String] = None,
                   variant: Option[String] = None,
                   onClick: Option[js.Any => Unit] = None,
                   onRemove: Option[js.Any => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onClick.nonEmpty) elementRef.current.addEventListener("click", props.onClick.get)
    if (props.onRemove.nonEmpty) elementRef.current.addEventListener("sl-remove", props.onRemove.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onClick.nonEmpty) elementRef.current.removeEventListener("click", props.onClick.get)
    if (props.onRemove.nonEmpty) elementRef.current.removeEventListener("sl-remove", props.onRemove.get)
  }

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.clearable, "clearable")
    add(attrs, props.className, "class")
    add(attrs, props.pill, "pill")
    add(attrs, props.removable, "removable")
    add(attrs, props.size, "size")
    add(attrs, props.variant, "variant")

    attrs += (ref := elementRef)

    val children = new ListBuffer[ReactElement]()
    children += props.label

    CustomTag("sl-tag")(attrs.toSeq: _*)(children.toSeq: _*)
  }
}