package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.React
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer

@react class MenuDivider extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  type Props = Unit

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    attrs += (ref := elementRef)
    CustomTag("sl-divider")(attrs: _*)()
  }
}