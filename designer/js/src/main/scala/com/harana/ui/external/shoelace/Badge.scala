package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer

@react class Badge extends StatelessComponent {

  case class Props(value: String,
                   className: Option[String] = None,
                   pill: Option[Boolean] = None,
                   pulse: Option[Boolean] = None,
                   slot: Option[String] = None,
                   `type`: Option[String] = None,
                   variant: Option[String] = None)

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.pill, "pill")
    add(attrs, props.pulse, "pulse")
    add(attrs, props.slot, "slot")
    add(attrs, props.`type`, "type")
    add(attrs, props.variant, "variant")

    CustomTag("sl-badge")(attrs: _*)(props.value)
  }
}