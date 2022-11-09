package com.harana.ui.external.diagrams

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-diagrams", "DefaultPortFactory")
class DefaultPortFactory extends js.Object {
  def generate(): DefaultPortModel[_,_] = js.native
}

trait DefaultPortModelGenerics extends PortModelGenerics {
  override val OPTIONS: DefaultPortModelOptions
}

trait DefaultPortModelOptions extends PortModelOptions {
  val label: js.UndefOr[String] = js.undefined
  val in: js.UndefOr[Boolean] = js.undefined
}

@js.native
@JSImport("@projectstorm/react-diagrams-defaults", "DefaultPortModel")
class DefaultPortModel[G <: DefaultPortModelGenerics, L <: PortModelListener](options: DefaultPortModelOptions) extends PortModel[G, L](options) {
  def link[T <: LinkModel[_,_]](port: PortModel[_,_]): T = js.native
  override def canLinkToPort(port: PortModel[_,_]): Boolean = js.native
  override def serialize(): js.Object = js.native
  override def deserialize(event: js.Object): Unit = js.native
}

trait PortModelListener extends BasePositionModelListener {
  def reportInitialPosition: BaseEntityEvent[PortModel[_,_]] => Unit
}

trait PortModelOptions extends BasePositionModelOptions {
  val alignment: js.UndefOr[String] = js.undefined
  val maximumLinks: js.UndefOr[Int] = js.undefined
  val name: js.UndefOr[String] = js.undefined
}

trait PortModelGenerics extends BasePositionModelGenerics {
  override val PARENT: NodeModel[_,_]
  override val OPTIONS: PortModelOptions 
  override val LISTENER: PortModelListener 
}

@js.native
@JSImport("@projectstorm/react-diagrams-core", "PortModel")
class PortModel[G <: PortModelGenerics, L <: PortModelListener](options: PortModelOptions) extends BasePositionModel[G, L](options) {
  def setPosition(point: PointModel[_,_]): Unit = js.native
  def getNode(): NodeModel[_,_] = js.native
  def getName(): String = js.native
  def getMaximumLinks(): Int = js.native
  def setMaximumLinks(maximumLinks: Int): Unit = js.native
  def removeLink(link: LinkModel[_,_]): Unit = js.native
  def addLink(link: LinkModel[_,_]): Unit = js.native
  def getLinks(): js.Array[LinkModel[_,_]] = js.native
  def createLink: LinkModel[_,_] = js.native
  def getCenter(): PointModel[_,_] = js.native
  def updateCoords(coords: Rectangle): Unit = js.native
  def canLinkToPort(port: PortModel[_,_]): Boolean = js.native
  override def setPosition(x: Int, y: Int): Unit = js.native
  override def isLocked(): Boolean = js.native
  override def serialize(): js.Object = js.native
  override def deserialize(event: js.Object): Unit = js.native
}