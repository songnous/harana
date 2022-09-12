package com.harana.ui.external.split_pane

import com.harana.ui.external.split_pane.Types.Size
import org.scalajs.dom.{MouseEvent, TouchEvent}
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-split-pane", JSImport.Default)
@js.native
object ReactSplitPane extends js.Object {
  def onMouseDown(event: MouseEvent): Unit = js.native
  def onMouseMove(event: MouseEvent): Unit = js.native
  def onMouseUp(): Unit = js.native
  def onTouchMove(event: TouchEvent): Unit = js.native
  def onTouchStart(event: TouchEvent): Unit = js.native
}

@react object SplitPane extends ExternalComponent {

  case class Props(allowResize: Option[Boolean] = None,
                   className: Option[String] = None,
                   defaultSize: Option[Size] = None,
                   maxSize: Option[Size] = None,
                   minSize: Option[Size] = None,
                   onChange: Option[Double => Unit] = None,
                   onDragFinished: Option[Double => Unit] = None,
                   onDragStarted: Option[() => Unit] = None,
                   onResizerClick: Option[MouseEvent => Unit] = None,
                   onResizerDoubleClick: Option[MouseEvent => Unit] = None,
                   pane1Style: Option[CSSProperties] = None,
                   pane2Style: Option[CSSProperties] = None,
                   paneStyle: Option[CSSProperties] = None,
                   primary: Option[String] = None,
                   resizerClassName: Option[String] = None,
                   resizerStyle: Option[CSSProperties] = None,
                   size: Option[Size] = None,
                   split: String,
                   step: Option[Double] = None,
                   style: Option[CSSProperties] = None)

  override val component = ReactSplitPane
}

object Types {
  type Partial[T] = String with T
  type Size = String | Double
}