package com.harana.ui.external.xterm

import slinky.core.ExternalComponentWithRefType
import slinky.core.annotations.react
import typings.react.mod.KeyboardEvent

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("updated-xterm-for-react", "XTerm")
@js.native
object ReactXTerm extends js.Object

@js.native trait XTermApi extends js.Object {
  val terminal: Terminal = js.native
}

@JSImport("xterm", "IBufferNamespace")
@js.native
class BufferNamespace extends js.Object {
  val active: Buffer = js.native
  val alternate: Buffer = js.native
  val normal: Buffer = js.native
  def onBufferChange(): Unit = js.native
}

@JSImport("xterm", "IBuffer")
@js.native
class Buffer extends js.Object {
  val baseY: Int = js.native
  val cursorX: Int = js.native
  val cursorY: Int = js.native
  val length: Int = js.native
  val `type`: String = js.native
  val viewportY: Int = js.native
  def getLine(line: Int): BufferLine = js.native
}

@JSImport("xterm", "IBufferLine")
@js.native
class BufferLine extends js.Object {
  val isWrapped: Boolean = js.native
  val length: Int = js.native
  def translateToString(trimRight: Option[Boolean] = None,
                        startColumn: Option[Int] = None,
                        endColumn: Option[Int] = None): String = js.native
}

@JSImport("xterm", "Terminal")
@js.native
class Terminal extends js.Object {
  val buffer: BufferNamespace = js.native
  def blur(): Unit = js.native
  def clear(): Unit = js.native
  def clearSelection(): Unit = js.native
  def focus(): Unit = js.native
  def getOption(key: String): js.Any = js.native
  def getSelection(): String = js.native
  def hasSelection(): Boolean = js.native
  def paste(data: String): Unit = js.native
  def refresh(start: Int, end: Int): Unit = js.native
  def reset(): Unit = js.native
  def select(column: Int, row: Int, length: Int): Unit = js.native
  def selectAll(): Unit = js.native
  def selectLines(start: Int, end: Int): Unit = js.native
  def setOption(key: String, value: js.Any): Unit = js.native
  def scrollLines(count: Int): Unit = js.native
  def scrollPages(count: Int): Unit = js.native
  def scrollToBottom(): Unit = js.native
  def scrollToLine(line: Int): Unit = js.native
  def scrollToTop(): Unit = js.native
  def write(line: String): Unit = js.native
  def writeln(line: String): Unit = js.native
}

@react object XTerm extends ExternalComponentWithRefType[XTermApi] {

  case class Props(className: Option[String] = None,
                   options: Option[TerminalOptions] = None,
                   onBinary: Option[String => Unit] = None,
                   onData: Option[String => Unit] = None,
                   onKey: Option[KeyEvent => Unit] = None,
                   onLineFeed: Option[Unit => Unit] = None,
                   onRender: Option[RenderEvent => Unit] = None)

    override val component = ReactXTerm
}

trait KeyEvent extends js.Object {
  val key: js.UndefOr[String] = js.undefined
  val domEvent: js.UndefOr[KeyboardEvent[_]] = js.undefined
}

trait RenderEvent extends js.Object {
  val start: js.UndefOr[Int] = js.undefined
  val end: js.UndefOr[Int] = js.undefined
}

trait TerminalOptions extends js.Object {
  val allowProposedApi: js.UndefOr[Boolean] = js.undefined
  val allowTransparency: js.UndefOr[Boolean] = js.undefined
  val cursorBlink: js.UndefOr[Boolean] = js.undefined
  val cursorStyle: js.UndefOr[String] = js.undefined
}