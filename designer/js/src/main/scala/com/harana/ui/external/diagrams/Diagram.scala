package com.harana.ui.external.diagrams

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

trait NodesUpdatedEvent extends BaseEntityEvent[NodeModel[_,_]] {
  val node: NodeModel[_,_]
  val isCreated: Boolean
}

trait LinksUpdatedEvent extends BaseEntityEvent[LinkModel[_,_]] {
  val link: LinkModel[_,_]
  val isCreated: Boolean
}

trait DiagramModelListener extends CanvasModelListener {
  def nodesUpdated(event: NodesUpdatedEvent): Unit
  def linksUpdated(event: LinksUpdatedEvent): Unit
}

trait DiagramModelGenerics extends CanvasModelGenerics {
  override val LISTENER: DiagramModelListener
}

@js.native
@JSImport("@projectstorm/react-diagrams-core", "DiagramModel")
class DiagramModel[G <: DiagramModelGenerics, L <: DiagramModelListener](options: CanvasModelOptions) extends CanvasModel[G, L](options) {
  def getLinkLayers(): js.Array[LinkLayerModel[_,_]] = js.native
  def getNodeLayers(): js.Array[NodeLayerModel[_,_]] = js.native
  def getActiveLinkLayer(): js.Array[LinkLayerModel[_,_]] = js.native
  def getActiveNodeLayer(): js.Array[NodeLayerModel[_,_]] = js.native
  def getLink(link: String): LinkModel[_,_] = js.native
  def getNode(node: String): NodeModel[_,_] = js.native
  def addLink(link: LinkModel[_,_]): LinkModel[_,_] = js.native
  def addNode(node: NodeModel[_,_]): NodeModel[_,_] = js.native
  def removeLink(link: LinkModel[_,_]): LinkModel[_,_] = js.native
  def removeNode(node: NodeModel[_,_]): NodeModel[_,_] = js.native
  def getLinks(): js.Array[LinkModel[_,_]] = js.native
  def getNodes(): js.Array[NodeModel[_,_]] = js.native
  override def addLayer(layer: LayerModel[_,_]): Unit = js.native
  override def deserialize(event: js.Object): Unit = js.native
  override def serialize(): js.Object = js.native
}