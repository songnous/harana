package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Spinner extends StatelessComponent {

  case class Props(className: Option[String] = None,
                   indicatorColor: Option[String] = None,
                   speed: Option[Int] = None,
                   trackColor: Option[String] = None,
                   trackWidth: Option[Int] = None)

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")

    val style = js.Dynamic.literal()
    if (props.indicatorColor.nonEmpty) style.updateDynamic("--indicator-color")(props.indicatorColor.get)
    if (props.speed.nonEmpty) style.updateDynamic("--color")(props.speed.get)
    if (props.trackColor.nonEmpty) style.updateDynamic("--track-color")(props.trackColor.get)
    if (props.trackWidth.nonEmpty) style.updateDynamic("--track-width")(props.trackWidth.get)
    add(attrs, Some(style), "style")

    CustomTag("sl-spinner")(attrs.toSeq: _*)
  }
}