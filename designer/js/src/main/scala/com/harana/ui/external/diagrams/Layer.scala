package com.harana.ui.external.diagrams

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

trait LayerModelOptions extends BaseModelOptions {
  val isSvg: js.UndefOr[Boolean] = js.undefined
  val transformed: js.UndefOr[Boolean] = js.undefined
}

trait LayerModelGenerics extends BaseModelGenerics {
  val CHILDREN: BaseModel[_,_]
  override val OPTIONS: LayerModelOptions
  override val PARENT: CanvasModel[_,_]
}

@js.native
@JSImport("@projectstorm/react-canvas-core", "LayerModel")
class LayerModel[G <: LayerModelGenerics, L <: BaseModelListener](options: LayerModelOptions = js.Dynamic.literal().asInstanceOf[LayerModelOptions]) extends BaseModel[G, L](options) {
  def isRepaintEnabled(): Boolean = js.native
  def allowRepaint(allow: Boolean = true): Unit = js.native
  def addModel(model: BaseModel[_,_]): Unit = js.native
  def getModels: js.Array[BaseModel[_,_]] = js.native
  def getModel(id: String): BaseModel[_,_] = js.native
  def removeModel(id: String): Boolean = js.native
  override def remove(): Unit = js.native
  override def getSelectionEntities: js.Array[BaseModel[_,_]] = js.native
  override def serialize(): js.Object = js.native
  override def deserialize(event: js.Object): Unit = js.native
}