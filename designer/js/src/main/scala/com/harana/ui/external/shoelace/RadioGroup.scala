package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class RadioGroup extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(children: List[ReactElement],
                   fieldset: Option[Boolean] = None,
                   label: Option[String] = None)

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.fieldset, "fieldset")
    add(attrs, props.label, "label")

    attrs += (ref := elementRef)

    val children = new ListBuffer[ReactElement]()
    if (props.label.isDefined) children += props.label.get

    CustomTag("sl-radio-group")(attrs.toSeq: _*)(children.toSeq: _*)
  }
}