package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{CustomTag, StatelessComponent}

import scala.collection.mutable.ListBuffer

@react class MenuLabel extends StatelessComponent {

  case class Props(label: String)

	override def shouldComponentUpdate(nextProps: Props, nextState: Unit) = !props.label.equals(nextProps.label)

  def render() = {
    val children = new ListBuffer[ReactElement]()
    children += props.label

    CustomTag("sl-menu-label")()(children: _*)
  }
}