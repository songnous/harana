package com.harana.ui.external.diagrams

import typings.std.SVGPathElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

trait ColorChangedEvent extends BaseEntityEvent[DefaultLinkModel[_,_]] {
  val color: String
}

trait WidthChangedEvent extends BaseEntityEvent[DefaultLinkModel[_,_]] {
  val width: Int
}

trait DefaultLinkModelListener extends LinkModelListener {
  def colorChanged(event: ColorChangedEvent): Unit
  def widthChanged(event: WidthChangedEvent): Unit
}

trait DefaultLinkModelGenerics extends LinkModelGenerics {
  override val LISTENER: DefaultLinkModelListener
  override val OPTIONS: DefaultLinkModelOptions
}

trait DefaultLinkModelOptions extends BaseModelOptions {
  val width: js.UndefOr[Int] = js.undefined
  val color: js.UndefOr[String] = js.undefined
  val selectedColor: js.UndefOr[String] = js.undefined
  val curvyness: js.UndefOr[Int] = js.undefined
  val testName: js.UndefOr[String] = js.undefined
  override val `type`: js.UndefOr[String] = js.undefined
}

@js.native
@JSImport("@projectstorm/react-diagrams-defaults", "DefaultLinkModel")
class DefaultLinkModel[G <: DefaultLinkModelGenerics, L <: DefaultLinkModelListener](options: DefaultLinkModelOptions) extends LinkModel[G, L](options) {
  def calculateControlOffset(port: PortModel[_,_]): (Int, Int) = js.native
  def getSVGPath: String = js.native
  def addLabel(label: LabelModel[_,_] | String): Unit = js.native
  def setWidth(width: Int): Unit = js.native
  def setColor(color: String): Unit = js.native
  override def deserialize(event: js.Object): Unit = js.native
  override def serialize(): js.Object = js.native
}

trait LinkModelListener extends BaseModelListener {
  def sourcePortChanged: BaseEntityEvent[LinkModel[_,_]]
  def targetedPortChanged: BaseEntityEvent[LinkModel[_,_]]
}

trait LinkModelGenerics extends BaseModelGenerics {
  override val LISTENER: LinkModelListener
  override val PARENT: DiagramModel[_,_]
}

@js.native
@JSImport("@projectstorm/react-diagrams-core", "LinkModel")
class LinkModel[G <: LinkModelGenerics, L <: LinkModelListener](options: BaseModelOptions) extends BaseModel[G, L](options){
  def getRenderedPath: SVGPathElement = js.native
  def setRenderedPaths(paths: js.Array[SVGPathElement]): Unit = js.native
  def clearPort(port: PortModel[_,_]): Unit = js.native
  def isLastPoint(point: PointModel[_,_]): Boolean = js.native
  def getPointIndex(point: PointModel[_,_]): Int = js.native
  def getPointModel(id: String): PointModel[_,_] = js.native
  def getPortForPoint(point: PointModel[_,_]): PortModel[_,_] = js.native
  def getPointForPort(port: PortModel[_,_]): PointModel[_,_] = js.native
  def getFirstPoint: PointModel[_,_] = js.native
  def getLastPoint: PointModel[_,_] = js.native
  def setSourcePort(port: PortModel[_,_]): Unit = js.native
  def setTargetPort(port: PortModel[_,_]): Unit = js.native
  def getSourcePort: PortModel[_,_] = js.native
  def point(x: Int, y: Int): PointModel[_,_] = js.native
  def addLabel(label: LabelModel[_,_]): Unit = js.native
  def getPoints: js.Array[PointModel[_,_]] = js.native
  def getLabels: js.Array[LabelModel[_,_]] = js.native
  def setPoints(points: js.Array[PointModel[_,_]]): Unit = js.native
  def removePoint(point: PointModel[_,_]): js.Array[PointModel[_,_]] = js.native
  def removePointsBefore(point: PointModel[_,_]): js.Array[PointModel[_,_]] = js.native
  def removePointsAfter(point: PointModel[_,_]): js.Array[PointModel[_,_]] = js.native
  def removeMiddlePoints(): js.Array[PointModel[_,_]] = js.native
  def addPoint[P <: PointModel[_,_]](point: P, index: Int = 1): P = js.native
  def generatePoint(x: Int = 0, y: Int = 0): PointModel[_,_] = js.native
  override def getSelectionEntities: js.Array[BaseModel[_,_]] = js.native
  override def remove(): Unit = js.native
  override def serialize(): js.Object = js.native
  override def deserialize(event: js.Object): Unit = js.native
}