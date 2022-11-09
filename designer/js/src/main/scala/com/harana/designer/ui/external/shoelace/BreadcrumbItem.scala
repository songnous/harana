package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer

@react class BreadcrumbItem extends StatelessComponent {

  case class Props(children: List[ReactElement] = List(),
                   href: Option[String] = None,
                   rel: Option[String] = None,
                   slot: Option[String] = None,
                   target: Option[String] = None)

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.href, "href")
    add(attrs, props.rel, "rel")
    add(attrs, props.slot, "slot")
    add(attrs, props.target, "target")

    CustomTag("sl-breadcrumb-item")(attrs.toSeq: _*)(props.children)
  }
}