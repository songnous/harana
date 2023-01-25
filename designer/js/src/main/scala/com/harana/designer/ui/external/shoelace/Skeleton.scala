package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Skeleton extends StatelessComponent {

  case class Props(borderRadius: Option[Int] = None,
                   className: Option[String] = None,
                   color: Option[String] = None,
                   effect: Option[String] = None,
                   sheenColor: Option[String] = None)

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.effect, "effect")

    val style = js.Dynamic.literal()
    if (props.borderRadius.nonEmpty) style.updateDynamic("--border-radius")(props.borderRadius.get)
    if (props.color.nonEmpty) style.updateDynamic("--color")(props.color.get)
    if (props.sheenColor.nonEmpty) style.updateDynamic("--sheen-color")(props.sheenColor.get)
    add(attrs, Some(style), "style")

    CustomTag("sl-skeleton")(attrs.toSeq: _*)
  }
}