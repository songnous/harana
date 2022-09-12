package com.harana.ui.external.diagrams

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

trait DefaultNodeModelOptions extends BasePositionModelOptions {
  val name: String
  val color: String
}

trait DefaultNodeModelGenerics extends NodeModelGenerics {
  override val OPTIONS: DefaultNodeModelOptions
}

@js.native
@JSImport("@projectstorm/react-diagrams-defaults", "DefaultNodeModel")
class DefaultNodeModel[G <: DefaultNodeModelGenerics, L <: NodeModelListener](options: DefaultNodeModelOptions) extends NodeModel[G, L](options) {
  def addPort[P <: DefaultPortModel[_,_]](port: P): P = js.native
  def removePort(port: DefaultPortModel[_,_]): Unit = js.native
  def addInPort[PG <: DefaultPortModelGenerics, PL <: PortModelListener](label: String, after: Boolean = false): DefaultPortModel[PG, PL] = js.native
  def addOutPort[PG <: DefaultPortModelGenerics, PL <: PortModelListener](label: String, after: Boolean = false): DefaultPortModel[PG,PL] = js.native
  def getInPorts(): js.Array[DefaultPortModel[_,_]] = js.native
  def getOutPorts(): js.Array[DefaultPortModel[_,_]] = js.native
  override def serialize(): js.Object = js.native
  override def deserialize(event: js.Object): Unit = js.native
}

trait NodeModelListener extends BasePositionModelListener

trait NodeModelGenerics extends BasePositionModelGenerics {
  override val LISTENER: NodeModelListener
  override val PARENT: DiagramModel[_,_]
}

@js.native
@JSImport("@projectstorm/react-diagrams-core", "NodeModel")
class NodeModel[G <: NodeModelGenerics, L <: NodeModelListener](options: BasePositionModelOptions) extends BasePositionModel[G, L](options) {
  def setPosition(point: PointModel[_,_]): Unit = js.native
  def getPortFromID(id: String): PortModel[_,_] = js.native
  def getLink(id: String): LinkModel[_,_] = js.native
  def getPort(name: String): PortModel[_,_] = js.native
  def getPorts: List[PortModel[_,_]] = js.native
  def removePort(port: PortModel[_,_]): Unit = js.native
  def addPort(port: PortModel[_,_]): Unit = js.native
  def updateDimensions(point: Point): Point = js.native
  override def setPosition(x: Int, y: Int): Unit = js.native
  override def remove(): Unit = js.native
  override def serialize(): js.Object = js.native
  override def deserialize(event: js.Object): Unit = js.native
}