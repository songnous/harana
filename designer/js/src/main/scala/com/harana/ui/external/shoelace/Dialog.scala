package com.harana.ui.external.shoelace

import org.scalajs.dom.HTMLElement
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{CustomTag, StatelessComponent, TagMod}
import slinky.web.html.{header, ref}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal

@react class Dialog extends StatelessComponent {

  val elementRef = React.createRef[HTMLElement]

  case class Props(children: List[ReactElement],
                   bodySpacing: Option[String] = None,
                   className: Option[String] = None,
                   footerSpacing: Option[String] = None,
                   headerElement: Option[ReactElement] = None,
                   headerSpacing: Option[String] = None,
                   label: Option[String] = None,
                   noHeader: Option[Boolean] = None,
                   open: Option[Boolean] = None,
                   slot: Option[String] = None,
                   width: Option[String] = None,
                   onAfterHide: Option[js.Any => Unit] = None,
                   onAfterShow: Option[js.Any => Unit] = None,
                   onHide: Option[js.Any => Unit] = None,
                   onInitialFocus: Option[js.Any => Unit] = None,
                   onOverlayDismiss: Option[js.Any => Unit] = None,
                   onRequestClose: Option[js.Any => Unit] = None,
                   onShow: Option[js.Any => Unit] = None)

  override def componentDidMount(): Unit = {
    if (props.onAfterHide.isDefined) elementRef.current.addEventListener("sl-after-hide", props.onAfterHide.get)
    if (props.onAfterShow.isDefined) elementRef.current.addEventListener("sl-after-show", props.onAfterShow.get)
    if (props.onHide.isDefined) elementRef.current.addEventListener("sl-hide", props.onHide.get)
    if (props.onInitialFocus.isDefined) elementRef.current.addEventListener("sl-initial-focus", props.onInitialFocus.get)
    if (props.onOverlayDismiss.isDefined) elementRef.current.addEventListener("sl-overlay-dismiss", props.onOverlayDismiss.get)
    if (props.onRequestClose.isDefined) elementRef.current.addEventListener("sl-request-close", props.onRequestClose.get)
    if (props.onShow.isDefined) elementRef.current.addEventListener("sl-show", props.onShow.get)
  }

  override def componentWillUnmount(): Unit = {
    if (props.onAfterHide.isDefined) elementRef.current.removeEventListener("sl-after-hide", props.onAfterHide.get)
    if (props.onAfterShow.isDefined) elementRef.current.removeEventListener("sl-after-show", props.onAfterShow.get)
    if (props.onInitialFocus.isDefined) elementRef.current.removeEventListener("sl-initial-focus", props.onInitialFocus.get)
    if (props.onOverlayDismiss.isDefined) elementRef.current.removeEventListener("sl-overlay-dismiss", props.onOverlayDismiss.get)
    if (props.onHide.isDefined) elementRef.current.removeEventListener("sl-hide", props.onHide.get)
    if (props.onRequestClose.isDefined) elementRef.current.removeEventListener("sl-request-close", props.onRequestClose.get)
    if (props.onShow.isDefined) elementRef.current.removeEventListener("sl-show", props.onShow.get)
  }

  def show() =
    elementRef.current.asInstanceOf[js.Dynamic].show()

  def hide() =
    elementRef.current.asInstanceOf[js.Dynamic].hide()

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.className, "class")
    add(attrs, props.label, "label")
    add(attrs, props.noHeader, "no-header")
    add(attrs, props.open, "open")
    add(attrs, props.slot, "slot")

    val style = js.Dynamic.literal()
    if (props.bodySpacing.isDefined) style.updateDynamic("--body-spacing")(props.bodySpacing.get)
    if (props.footerSpacing.isDefined) style.updateDynamic("--footer-spacing")(props.headerSpacing.get)
    if (props.headerSpacing.isDefined) style.updateDynamic("--header-spacing")(props.headerSpacing.get)
    if (props.width.isDefined) style.updateDynamic("--width")(props.width.get)
    add(attrs, Some(style), "style")

    attrs += (ref := elementRef)

    val headerChild: Option[ReactElement] = props.headerElement.map(he => header(he))
    val children = if (headerChild.isDefined) List(headerChild.get) ++ props.children else props.children
    CustomTag("sl-dialog")(attrs.toSeq: _*)(children.toSeq: _*)
  }
}