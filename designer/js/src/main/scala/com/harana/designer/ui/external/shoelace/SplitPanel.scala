package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class SplitPanel extends StatelessComponent {

  case class Props(disabled: Option[Boolean] = None,
                   dividerHitArea: Option[String] = None,
                   dividerWidth: Option[String] = None,
                   minSize: Option[Int] = None,
                   maxSize: Option[Int] = None,
                   position: Option[Int] = None,
                   positionInPixels: Option[Int] = None,
                   primary: Option[String] = None,
                   snap: Option[String] = None,
                   snapThreshold: Option[Int] = None,
                   vertical: Option[Boolean] = None)

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.disabled, "disabled")
    add(attrs, props.position, "position")
    add(attrs, props.positionInPixels, "position-in-pixels")
    add(attrs, props.snap, "snap")
    add(attrs, props.snapThreshold, "snap-threshold")
    add(attrs, props.vertical, "vertical")

    val style = js.Dynamic.literal()
    if (props.dividerHitArea.nonEmpty) style.updateDynamic("--divider-hit-area")(props.dividerHitArea.get)
    if (props.dividerWidth.nonEmpty) style.updateDynamic("--divider-width")(props.dividerWidth.get)
    if (props.minSize.nonEmpty) style.updateDynamic("--min")(props.minSize.get)
    if (props.maxSize.nonEmpty) style.updateDynamic("--max")(props.maxSize.get)
    add(attrs, Some(style), "style")

    CustomTag("sl-split-panel")(attrs.toSeq: _*)
  }
}