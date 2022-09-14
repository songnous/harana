package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer

@react class Card extends StatelessComponent {

  case class Props(className: Option[String] = None,
                   slot: Option[String] = None,
                   children: List[ReactElement])

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.slot, "slot")

    CustomTag("sl-card")(attrs.toSeq: _*)(props.children)
  }
}