package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.ref
import com.harana.sdk.shared.utils.Random

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class Dropdown extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(button: Option[Button.Props] = None,
                   buttonKey: Option[String] = None,
                   element: Option[ReactElement] = None,
                   className: Option[String] = None,
                   closeOnSelect: Option[String] = None,
                   containingElement: Option[HTMLElement] = None,
                   disabled: Option[Boolean] = None,
                   distance: Option[Int] = None,
                   hoist: Option[Boolean] = None,
                   menu: Option[Menu.Props] = None,
                   open: Option[Boolean] = None,
                   placement: Option[String] = None,
                   skidding: Option[Int] = None,
                   stayOpenOnSelect: Option[Boolean] = None,
                   slot: Option[String] = None,
                   onAfterHide: Option[js.Any => Unit] = None,
                   onAfterShow: Option[js.Any => Unit] = None,
                   onHide: Option[js.Any => Unit] = None,
                   onShow: Option[js.Any => Unit] = None,
                   tooltip: Option[String] = None,
                   tooltipKey: Option[String] = None)

  override def componentDidMount(): Unit = {
    if (props.onAfterHide.isDefined) elementRef.current.addEventListener("sl-after-hide", props.onAfterHide.get)
    if (props.onAfterShow.isDefined) elementRef.current.addEventListener("sl-after-show", props.onAfterShow.get)
    if (props.onHide.isDefined) elementRef.current.addEventListener("sl-hide", props.onHide.get)
    if (props.onShow.isDefined) elementRef.current.addEventListener("sl-show", props.onShow.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onAfterHide.isDefined) elementRef.current.removeEventListener("sl-after-hide", props.onAfterHide.get)
    if (props.onAfterShow.isDefined) elementRef.current.removeEventListener("sl-after-show", props.onAfterShow.get)
    if (props.onHide.isDefined) elementRef.current.removeEventListener("sl-hide", props.onHide.get)
    if (props.onShow.isDefined) elementRef.current.removeEventListener("sl-show", props.onShow.get)
  }

  def show() =
    elementRef.current.asInstanceOf[js.Dynamic].show()

  def hide() =
    elementRef.current.asInstanceOf[js.Dynamic].hide()

  def reposition() =
    elementRef.current.asInstanceOf[js.Dynamic].reposition()

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.closeOnSelect, "close-on-select")
    add(attrs, props.containingElement, "containingElement")
    add(attrs, props.disabled, "disabled")
    add(attrs, props.distance, "distance")
    add(attrs, props.hoist, "hoist")
    add(attrs, props.open, "open")
    add(attrs, props.placement, "placement")
    add(attrs, props.skidding, "skidding")
    add(attrs, props.stayOpenOnSelect, "stay-open-on-select")

    attrs += (ref := elementRef)

    val children = new ListBuffer[ReactElement]()
    if (props.button.isDefined) children += List(
      if (props.tooltip.isDefined)
        Tooltip(props.tooltip.get, slot = Some("trigger"))(List(Button(props.button.get.copy(slot = None))))
      else
        Button(props.button.get.copy(slot = Some("trigger")))
    )

    if (props.element.isDefined) children += props.element.get
    if (props.menu.isDefined) children += Menu(props.menu.get)

    CustomTag("sl-dropdown")(attrs: _*)(children: _*)
  }
}