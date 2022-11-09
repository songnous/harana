package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer

@react class Breadcrumb extends StatelessComponent {

  case class Props(items: List[ReactElement] = List())

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()

    CustomTag("sl-breadcrumb")(attrs.toSeq: _*)(props.items)
  }
}