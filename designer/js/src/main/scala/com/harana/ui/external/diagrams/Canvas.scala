package com.harana.ui.external.diagrams

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

trait OffsetEvent extends BaseEntityEvent[CanvasModel[_,_]] {
  val offsetX: Int
  val offsetY: Int
}

trait ZoomEvent extends BaseEntityEvent[CanvasModel[_,_]] {
  val zoom: Int
}

trait GridUpdatedEvent extends BaseEntityEvent[CanvasModel[_,_]] {
  val size: Int
}

trait CanvasModelListener extends BaseEntityListener[CanvasModel[_,_]] {
  def offsetUpdated(event: OffsetEvent): Unit
  def zoomUpdated(event: ZoomEvent): Unit
  def gridUpdatedEvent(event: GridUpdatedEvent): Unit
}

trait CanvasModelOptions extends BaseEntityOptions {
  val offsetX: js.UndefOr[Int] = js.undefined
  val offsetY: js.UndefOr[Int] = js.undefined
  val zoom: js.UndefOr[Int] = js.undefined
  val gridSize: js.UndefOr[Int] = js.undefined
}

trait CanvasModelGenerics extends BaseEntityGenerics {
  val LAYER: LayerModel[_,_]
  override val LISTENER: CanvasModelListener
  override val OPTIONS: CanvasModelOptions
}

@js.native
@JSImport("@projectstorm/react-canvas-core", "CanvasModel")
class CanvasModel[G <: CanvasModelGenerics, L <: CanvasModelListener](options: CanvasModelOptions) extends BaseEntity[G, L] {
  def getSelectionEntities(): js.Array[BaseModel[_,_]] = js.native
  def getSelectedEntities(): js.Array[BaseModel[_,_]] = js.native
  def clearSelection(): js.Array[BaseModel[_,_]] = js.native
  def getModels(): js.Array[BaseModel[_,_]] = js.native
  def addLayer(layer: LayerModel[_,_]): Unit = js.native
  def removeLayer(layer: LayerModel[_,_]): Unit = js.native
  def getLayers(): js.Array[LayerModel[_,_]] = js.native
  def setGridSize(size: Int = 0): Unit = js.native
  def getGridPosition(pos: Int): Int = js.native
  def setZoomLevel(zoom: Int): Unit = js.native
  def setOffset(offsetX: Int, offsetY: Int): Unit = js.native
  def setOffsetX(offsetX: Int): Unit = js.native
  def setOffsetY(offsetY: Int): Unit = js.native
  def getOffsetX(): Int = js.native
  def getOffsetY(): Int = js.native
  def getZoomLevel(): Int = js.native
  override def serialize(): js.Object = js.native
  override def deserialize(event: js.Object): Unit = js.native
}