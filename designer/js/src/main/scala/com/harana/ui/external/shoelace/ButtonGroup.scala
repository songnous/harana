package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer

@react class ButtonGroup extends StatelessComponent {

  case class Props(children: List[ReactElement] = List(),
                   className: Option[String] = None,
                   label: Option[String] = None,
                   slot: Option[String] = None)

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.label, "label")
    add(attrs, props.slot, "slot")

    CustomTag("sl-button-group")(attrs: _*)(props.children)
  }
}