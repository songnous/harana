package com.harana.ui.external.diagrams

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

trait LinkLayerModelGenerics extends LayerModelGenerics {
  override val CHILDREN: LinkModel[_,_]
}

@js.native
@JSImport("@projectstorm/react-diagrams-core", "LinkLayerModel")
class LinkLayerModel[G <: LinkLayerModelGenerics, L <: BaseModelListener] extends LayerModel[G, L] {
  def addModel(model: LinkModel[_,_]): Unit = js.native
  def getLinks(): js.Array[LinkModel[_,_]] = js.native
}