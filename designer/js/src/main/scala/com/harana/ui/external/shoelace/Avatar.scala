package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer

@react class Avatar extends StatelessComponent {

  case class Props(className: Option[String] = None,
                   image: Option[String] = None,
                   initials: Option[String] = None,
                   label: Option[String] = None,
                   shape: Option[String] = None,
                   slot: Option[String] = None)

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.image, "image")
    add(attrs, props.initials, "initials")
    add(attrs, props.label, "label")
    add(attrs, props.shape, "shape")
    add(attrs, props.slot, "slot")

    CustomTag("sl-avatar")(attrs: _*)
  }
}