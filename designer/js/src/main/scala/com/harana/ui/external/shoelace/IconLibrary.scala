package com.harana.ui.external.shoelace

import org.scalajs.dom.{HTMLElement, SVGElement}
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class IconLibrary extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(name: String,
                   resolver: js.Function1[String, String],
                   mutator: Option[js.Function1[SVGElement, Unit]] = None)

  // override def componentDidMount(): Unit = {
  //   elementRef.asInstanceOf[js.Dynamic].updateDynamic("resolver")(props.resolver)
  // }

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, Some(props.name), "name")
    attrs += (ref := elementRef)

    CustomTag("sl-icon-library")(attrs: _*)
  }
}