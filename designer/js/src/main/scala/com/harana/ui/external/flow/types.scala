package com.harana.ui.external.flow

import com.harana.designer.frontend.flows.item.ui.ActionNodeData
import com.harana.ui.external.flow.types._

import scala.scalajs.js
import scala.scalajs.js.|

object types {
  type FlowNode = Node[ActionNodeData]
  type FlowElement = FlowNode | FlowEdge
  type FlowElementId = String
  type HandleType = String
  type Position = String
}

trait Box extends js.Object {
  val x: Int
  val x2: Int
  val y: Int
  val y2: Int
}

trait Dimensions extends js.Object {
  val width: Int
  val height: Int
}

trait Rect extends Dimensions with XYPosition

trait XYPosition extends js.Object {
  val x: Int
  val y: Int
}

trait SelectionRect extends Rect {
  val startX: Int
  val startY: Int
  val draw: Boolean
}

trait Connection extends Rect {
  val source: FlowElementId
  val target: FlowElementId
  val sourceHandle: String
  val targetHandle: String
}

trait SetConnectionId extends Rect {
  val connectionNodeId: FlowElementId
  val connectionHandleType: HandleType
}

trait HandleElement extends XYPosition with Dimensions {
  val id: js.UndefOr[FlowElementId] = js.undefined
  val position: String
}

trait NodePosUpdate extends js.Object {
  val id: FlowElementId
  val pos: XYPosition
}

trait NodeDiffUpdate extends js.Object {
  val id: FlowElementId
  val diff: js.UndefOr[XYPosition] = js.undefined
  val isDragging: js.UndefOr[Boolean] = js.undefined
}

trait FlowTransform extends js.Object {
  val x: Int
  val y: Int
  val zoom: Int
}

trait FitViewParams extends js.Object {
  val padding: Int
}

trait FlowInstance extends js.Object {
  def zoomIn(): Unit
  def zoomOut(): Unit
  def zoomTo(number: Int): Unit
  def fitView(params: js.UndefOr[FitViewParams] = js.undefined): Unit
  def project(position: XYPosition): XYPosition
  def getElements: List[FlowElement]
  def setTransform(transform: FlowTransform): Unit
}

trait OnConnectStartParams extends js.Object {
  val nodeId: js.UndefOr[FlowElementId] = js.undefined
  val handleType: js.UndefOr[HandleType] = js.undefined
}

trait FlowEdge extends js.Object {
  val id: FlowElementId
  val source: FlowElementId
  val target: FlowElementId
  val sourceHandle: FlowElementId
  val targetHandle: FlowElementId
  val `type`: js.UndefOr[String] = js.undefined
  val data: js.UndefOr[js.Object] = js.undefined
  val label: js.UndefOr[String] = js.undefined
  val labelStyle: js.UndefOr[js.Dynamic] = js.undefined
  val labelShowBg: js.UndefOr[Boolean] = js.undefined
  val labelBgStyle: js.UndefOr[js.Dynamic] = js.undefined
  val labelBgPadding: js.UndefOr[(Int, Int)] = js.undefined
  val labelBgBorderRadius: js.UndefOr[Int] = js.undefined
  val style: js.UndefOr[js.Dynamic] = js.undefined
  val animated: js.UndefOr[Boolean] = js.undefined
  val arrowHeadType: js.UndefOr[String] = js.undefined
  val isHidden: js.UndefOr[Boolean] = js.undefined
  val className: js.UndefOr[String] = js.undefined
  val removable: js.UndefOr[Boolean] = js.undefined
}

trait Node[T <: js.Object] extends js.Object {
  val id: FlowElementId
  val position: XYPosition
  val `type`: String
  val data: T
  val __rf: js.UndefOr[js.Any] = js.undefined
  val style: js.UndefOr[js.Dynamic] = js.undefined
  val className: js.UndefOr[String] = js.undefined
  val targetPosition: js.UndefOr[Position] = js.undefined
  val sourcePosition: js.UndefOr[Position] = js.undefined
  val isHidden: js.UndefOr[Boolean] = js.undefined
  val draggable: js.UndefOr[Boolean] = js.undefined
  val removable: js.UndefOr[Boolean] = js.undefined
  val selectable: js.UndefOr[Boolean] = js.undefined
  val connectable: js.UndefOr[Boolean] = js.undefined
}