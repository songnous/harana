package com.harana.ui.external.diagrams

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

trait BaseEvent extends js.Object {
  val firing: Boolean
  val stopPropagation: () => Unit
}

trait BaseListener extends js.Object {
  def eventWillFire: js.UndefOr[BaseEvent => Unit] = js.undefined
  def eventDidFire: js.UndefOr[BaseEvent => Unit] = js.undefined
}

trait SelectionChangedEvent extends BaseEntityEvent[BaseModel[_,_]] {
  val isSelected: Boolean
}

trait BaseEntityEvent[T <: BaseEntity[_,_]] extends BaseEvent {
  val entity: T
}

trait BaseEntityListener[T <: BaseEntity[_,_]] extends BaseListener {
  def lockChanged(event: BaseEntityEvent[T]): Unit
}

trait ListenerHandle extends js.Object {
  val deregister: () => Unit
  val id: String
  val listener: BaseListener
}

@js.native
@JSImport("@projectstorm/react-canvas-core", "BaseObserver")
class BaseObserver[L <: BaseListener] extends js.Object {
  def iterateListeners(cb: L => Any): Unit = js.native
  def getListenerHandle(listener: L): ListenerHandle = js.native
  def registerListener(listener: L): ListenerHandle = js.native
  def deregisterListener(listener: L | ListenerHandle): Unit = js.native
}

trait BaseEntityOptions extends js.Object {
  val id: js.UndefOr[String] = js.undefined
  val locked: js.UndefOr[Boolean] = js.undefined
}

trait BaseEntityGenerics extends js.Object {
  val LISTENER: BaseEntityListener[_]
  val OPTIONS: BaseEntityOptions
}

trait BaseModelListener extends BaseEntityListener[BaseModel[_,_]] {
  def selectionChanged(event: SelectionChangedEvent): Unit
  def entityRemoved(event: BaseEntityEvent[BaseModel[_,_]]): Unit
}

trait BaseModelOptions extends BaseEntityOptions {
  val `type`: js.UndefOr[String] = js.undefined
  val selected: js.UndefOr[Boolean] = js.undefined
  val extras: js.UndefOr[js.Any] = js.undefined
}

trait BaseModelGenerics extends BaseEntityGenerics {
  val PARENT: BaseEntity[_,_]
  override val LISTENER: BaseModelListener
  override val OPTIONS: BaseModelOptions
}

@js.native
@JSImport("@projectstorm/react-canvas-core", "BaseEntity")
class BaseEntity[G <: BaseEntityGenerics, L <: BaseEntityListener[_]] extends BaseObserver[L] {
  def getOptions(): BaseEntityOptions = js.native
  def getID(): js.Object = js.native
  def clearListeners(): Unit = js.native
  def isLocked(): Boolean = js.native
  def setLocked(locked: Boolean = true): Unit = js.native
  def deserialize(event: js.Object): Unit = js.native
  def serialize(): js.Object = js.native
}

@js.native
@JSImport("@projectstorm/react-canvas-core", "BaseModel")
class BaseModel[G <: BaseModelGenerics, L <: BaseEntityListener[_]](options: BaseModelOptions) extends BaseEntity[G, L] {
  def getParentCanvasModel: CanvasModel[_,_] = js.native
  def getParent(): BaseEntity[_,_] = js.native
  def setParent(parent: BaseEntity[_,_]): Unit = js.native
  def getSelectionEntities: js.Array[BaseModel[_,_]] = js.native
  def getType(): String = js.native
  def isSelected: Boolean = js.native
  def setSelected(selected: Boolean = true): Unit = js.native
  def remove(): Unit = js.native
  override def isLocked(): Boolean = js.native
  override def deserialize(event: js.Object): Unit = js.native
  override def serialize(): js.Object = js.native
}

trait BasePositionModelListener extends BaseModelListener {
  def positionChanged(event: BaseEntityEvent[BasePositionModel[_,_]]): Unit
}

trait BasePositionModelOptions extends BaseModelOptions {
  val position: js.UndefOr[Point] = js.undefined
}

trait Point extends js.Object {
  val x: Int
  val y: Int
}

trait Rectangle extends js.Object {
  def updateDimensions(x: Int, y: Int, width: Int, height: Int): Unit
  def setPoints(points: js.Array[Point]): Unit
  def containsPoint(point: Point): Boolean
  def getWidth: Int
  def getHeight: Int
  def getTopMiddle: Int
  def getBottomMiddle: Int
  def getLeftMiddle: Int
  def getRightMiddle: Int
  def getTopLeft: Int
  def getTopRight: Int
  def getBottomLeft: Int
  def getBottomRight: Int
}

trait BasePositionModelGenerics extends BaseModelGenerics {
  override val LISTENER: BasePositionModelListener
  override val OPTIONS: BasePositionModelOptions
}

@js.native
@JSImport("@projectstorm/react-canvas-core", "BasePositionModel")
class BasePositionModel[G <: BaseModelGenerics, L <: BasePositionModelListener](options: BasePositionModelOptions) extends BaseModel[G, L](options) {
  def setPosition(x: Int, y: Int): Unit = js.native
  def getBoundingBox: Rectangle = js.native
  def getPosition: Point = js.native
  def getX: Int = js.native
  def getY: Int = js.native
  override def deserialize(event: js.Object): Unit = js.native
  override def serialize(): js.Object = js.native
}