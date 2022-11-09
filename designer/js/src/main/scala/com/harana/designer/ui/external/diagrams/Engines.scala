package com.harana.ui.external.diagrams

import typings.react.mod.ReactElement
import typings.std.{Element, HTMLDivElement, MouseEvent}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

trait CanvasEngineListener extends BaseListener {
  def canvasReady(): Unit
  def repaintCanvas(): Unit
  def rendered(): Unit
}

trait CanvasEngineOptions extends js.Object {
  val registerDefaultDeleteItemsAction: js.UndefOr[Boolean]{} = js.undefined
  val registerDefaultZoomCanvasAction: js.UndefOr[Boolean] = js.undefined
}

trait CanvasPosition extends js.Object {
  val clientX: Int
  val clientY: Int
}

trait Size extends js.Object {
  val width: Int
  val height: Int
}

@js.native
@JSImport("@projectstorm/react-canvas-core", "CanvasEngine")
class CanvasEngine[L <: CanvasEngineListener, M <: CanvasModel[_,_]](options: CanvasEngineOptions) extends BaseObserver[L] {
  def getRelativeMousePoint(event: CanvasPosition): Point = js.native
  def getRelativePoint(x: Int, y: Int): Point = js.native
  def setModel(model: M): Unit = js.native
  def getModel(): M = js.native
  def repaintCanvas(): Unit = js.native
  def setCanvas(canvas: HTMLDivElement): Unit = js.native
  def getCanvas(): HTMLDivElement = js.native
  def getMouseElement(event: MouseEvent): BaseModel[_,_] = js.native
  def zoomToFit(): Unit = js.native
}

@js.native
@JSImport("@projectstorm/react-diagrams-core", "DiagramEngine")
class DiagramEngine(options: CanvasEngineOptions) extends CanvasEngine[CanvasEngineListener, DiagramModel[_,_]](options) {
  def generateWidgetForNode(node: NodeModel[_,_]): ReactElement = js.native
  def getNodeElement(node: NodeModel[_,_]): Element = js.native
  def getNodePortElement(port: PortModel[_,_]): js.Any = js.native
  def getPortCenter(port: PortModel[_,_]): Point = js.native
  def getPortCoords(port: PortModel[_,_], element: Option[HTMLDivElement]): Rectangle = js.native
  def getNodeDimensions(node: NodeModel[_,_]): Size = js.native
  def getMaxNumberPointsPerLink(): Int = js.native
  def setMaxNumberPointsPerLink(max: Int): Unit = js.native
  override def getMouseElement(event: MouseEvent): BaseModel[_,_] = js.native
}

@js.native
@JSImport("@projectstorm/react-diagrams", JSImport.Default)
object ReactDiagrams extends js.Object {
  def apply(): DiagramEngine = js.native
}