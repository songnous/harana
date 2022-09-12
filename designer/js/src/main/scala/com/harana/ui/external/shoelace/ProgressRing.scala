package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class ProgressRing extends StatelessComponent {

  case class Props(className: Option[String] = None,
                   indicatorColor: Option[String] = None,
                   label: Option[String] = None,
                   lang: Option[String] = None,
                   size: Option[Int] = None,
                   trackColor: Option[String] = None,
                   trackWidth: Option[Int] = None,
                   value: Option[Int] = None)

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.label, "label")
    add(attrs, props.lang, "lang")
    add(attrs, props.value, "value")

    val children = new ListBuffer[ReactElement]()
    if (props.label.isDefined) children += props.label.get

    val style = js.Dynamic.literal()
    if (props.indicatorColor.isDefined) style.updateDynamic("--indicator-color")(props.indicatorColor.get)
    if (props.trackColor.isDefined) style.updateDynamic("--track-color")(props.trackColor.get)
    if (props.trackWidth.isDefined) style.updateDynamic("--track-width")(props.trackWidth.get)
    if (props.size.isDefined) style.updateDynamic("--size")(props.size.get)
    add(attrs, Some(style), "style")

    CustomTag("sl-progress-ring")(attrs: _*)(children: _*)
  }
}