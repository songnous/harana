package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Tab extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(label: String,
                   panel: String,
                   active: Option[Boolean] = None,
                   className: Option[String] = None,
                   disabled: Option[Boolean] = None,
                   lang: Option[String] = None)

  def blur() =
    elementRef.current.asInstanceOf[js.Dynamic].blur()

  def focus() =
    elementRef.current.asInstanceOf[js.Dynamic].focus()

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.active, "active")
    add(attrs, props.className, "class")
    add(attrs, props.disabled, "disabled")
    add(attrs, props.lang, "lang")
    add(attrs, Some(props.panel), "panel")
    add(attrs, Some("nav"), "slot")

    val children = new ListBuffer[ReactElement]()
    children += props.label

    CustomTag("sl-tab")(attrs: _*)(children: _*)
  }
}