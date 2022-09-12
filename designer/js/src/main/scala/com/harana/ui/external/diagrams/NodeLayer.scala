package com.harana.ui.external.diagrams

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

trait NodeLayerModelGenerics extends LayerModelGenerics {
  override val CHILDREN: NodeModel[_,_]
}

@js.native
@JSImport("@projectstorm/react-diagrams-core", "NodeLayerModel")
class NodeLayerModel[G <: NodeLayerModelGenerics, L <: NodeModelListener] extends LayerModel[G, L] {
  def addModel(model: NodeModel[_,_]): Unit = js.native
  def getNodes(): js.Array[NodeModel[_,_]] = js.native
}