package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class TabPanel extends StatelessComponent {

  case class Props(name: String,
                   active: Option[Boolean] = None,
                   children: List[ReactElement] = List(),
                   className: Option[String] = None,
                   padding: Option[String] = None)

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.active, "active")
    add(attrs, props.className, "class")
    add(attrs, Some(props.name), "name")

    val style = js.Dynamic.literal()
    if (props.padding.isDefined) style.updateDynamic("--padding")(props.padding.get)
    add(attrs, Some(style), "style")

    CustomTag("sl-tab-panel")(attrs: _*)(props.children: _*)
  }
}