package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class ProgressBar extends StatelessComponent {

  case class Props(className: Option[String] = None,
                   height: Option[String] = None,
                   indeterminate: Option[Boolean] = None,
                   indicatorColor: Option[String] = None,
                   label: Option[String] = None,
                   labelColor: Option[String] = None,
                   lang: Option[String] = None,
                   trackColor: Option[String] = None,
                   value: Option[Int] = None)

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.indeterminate, "indeterminate")
    add(attrs, props.label, "label")
    add(attrs, props.lang, "lang")
    add(attrs, props.value, "value")

    val children = new ListBuffer[ReactElement]()
    if (props.label.nonEmpty) children += props.label.get

    val style = js.Dynamic.literal()
    if (props.height.nonEmpty) style.updateDynamic("--height")(props.height.get)
    if (props.indicatorColor.nonEmpty) style.updateDynamic("--indicator-color")(props.indicatorColor.get)
    if (props.labelColor.nonEmpty) style.updateDynamic("--label-color")(props.labelColor.get)
    if (props.trackColor.nonEmpty) style.updateDynamic("--track-color")(props.trackColor.get)
    add(attrs, Some(style), "style")

    CustomTag("sl-progress-bar")(attrs.toSeq: _*)(children.toSeq: _*)
  }
}