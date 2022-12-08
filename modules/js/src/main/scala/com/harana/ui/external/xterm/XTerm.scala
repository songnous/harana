package com.harana.ui.external.xterm

import org.scalajs.dom.Element
import slinky.core.ExternalComponentWithRefType
import slinky.core.annotations.react
import typings.react.mod.KeyboardEvent

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|


@JSImport("xterm/css/xterm.css", JSImport.Default)
@js.native
object XTermCSS extends js.Object

@JSImport("updated-xterm-for-react", "XTerm")
@js.native
object ReactXTerm extends js.Object

@js.native trait XTermApi extends js.Object {
  val terminal: Terminal = js.native
}

@JSImport("xterm-addon-fit", "FitAddon")
@js.native
class FitAddon extends js.Object {
  def fit(): Unit = js.native
  def proposeDimensions(): ITerminalDimensions = js.native
}

@JSImport("xterm-addon-fit", "ITerminalDimensions")
@js.native
class ITerminalDimensions extends js.Object {
  val rows: Int = js.native
  val cols: Int = js.native
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

@JSImport("xterm", "IEvent")
@js.native
class IEvent[T] extends js.Object {
  def addListener(fn: T => Unit) = js.native
}

@JSImport("xterm", "Terminal")
@js.native
class Terminal extends js.Object {
  val buffer: BufferNamespace = js.native
  val cols: Int = js.native
  val rows: Int = js.native
  def onBinary(fn: String => Unit) = js.native
  def onData(fn: js.Function1[String, Unit]) = js.native
  def onKey(fn: js.Function1[KeyEvent, Unit]) = js.native
  def onLineFeed(fn: js.Function0[Unit]) = js.native
  var options: TerminalOptions = js.native
  def blur(): Unit = js.native
  def clear(): Unit = js.native
  def clearSelection(): Unit = js.native
  def focus(): Unit = js.native
  def getOption(key: String): js.Any = js.native
  def getSelection(): String = js.native
  def hasSelection(): Boolean = js.native
  def loadAddon(addon: js.Object): Unit = js.native
  def open(element: Element): Unit = js.native
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
  var allowProposedApi: js.UndefOr[Boolean] = js.undefined
  var allowTransparency: js.UndefOr[Boolean] = js.undefined
  var cursorBlink: js.UndefOr[Boolean] = js.undefined
  var cursorStyle: js.UndefOr[String] = js.undefined
}