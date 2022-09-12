package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Divider extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(color: Option[String] = None,
                   vertical: Option[Boolean] = None,
                   width: Option[String] = None)

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.vertical, "vertical")

    val style = js.Dynamic.literal()
    if (props.color.isDefined) style.updateDynamic("--color")(props.color.get)
    if (props.width.isDefined) style.updateDynamic("--width")(props.width.get)
    add(attrs, Some(style), "style")

    attrs += (ref := elementRef)

    CustomTag("sl-divider")(attrs: _*)()
  }
}