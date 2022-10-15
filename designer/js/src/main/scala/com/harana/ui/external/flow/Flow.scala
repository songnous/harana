package com.harana.ui.external.flow

import com.harana.ui.external.flow.types.{FlowElement, FlowNode}
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.std.{MouseEvent, WheelEvent}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-flow-renderer", JSImport.Default)
@js.native
object ReactFlow extends js.Object

@react object Flow extends ExternalComponent {

  case class Props(nodes: List[ReactFlowNode[_]],
                   edges: List[ReactFlowEdge],
                   onElementClick: js.UndefOr[(MouseEvent, FlowElement) => Any] = js.undefined,
                   onElementsRemove: js.UndefOr[js.Array[FlowElement] => Any] = js.undefined,
                   onNodeMouseEnter: js.UndefOr[(MouseEvent, FlowNode) => Any] = js.undefined,
                   onNodeMouseMove: js.UndefOr[(MouseEvent, FlowNode) => Any] = js.undefined,
                   onNodeMouseLeave: js.UndefOr[(MouseEvent, FlowNode) => Any] = js.undefined,
                   onNodeContextMenu: js.UndefOr[(MouseEvent, FlowNode) => Any] = js.undefined,
                   onNodeDragStart: js.UndefOr[(MouseEvent, FlowNode) => Any] = js.undefined,
                   onNodeDragStop: js.UndefOr[(MouseEvent, FlowNode) => Any] = js.undefined,
                   onConnect: js.UndefOr[Connection => Any] = js.undefined,
                   onConnectStart: js.UndefOr[(MouseEvent, OnConnectStartParams) => Any] = js.undefined,
                   onConnectStop: js.UndefOr[MouseEvent => Any] = js.undefined,
                   onConnectEnd: js.UndefOr[MouseEvent => Any] = js.undefined,
                   onInit: js.UndefOr[FlowInstance => Any] = js.undefined,
                   onMove: js.UndefOr[FlowTransform => Any] = js.undefined,
                   onMoveStart: js.UndefOr[FlowTransform => Any] = js.undefined,
                   onMoveEnd: js.UndefOr[FlowTransform => Any] = js.undefined,
                   onSelectionChange: js.UndefOr[js.Array[FlowElement] => Any] = js.undefined,
                   onSelectionDragStart: js.UndefOr[(MouseEvent, js.Array[FlowNode]) => Any] = js.undefined,
                   onSelectionDrag: js.UndefOr[(MouseEvent, js.Array[FlowNode]) => Any] = js.undefined,
                   onSelectionDragStop: js.UndefOr[(MouseEvent, js.Array[FlowNode]) => Any] = js.undefined,
                   onSelectionContextMenu: js.UndefOr[(MouseEvent, js.Array[FlowNode]) => Any] = js.undefined,
                   onPaneScroll: js.UndefOr[WheelEvent => Any] = js.undefined,
                   onPaneClick: js.UndefOr[MouseEvent => Any] = js.undefined,
                   onPaneContextMenu: js.UndefOr[MouseEvent => Any] = js.undefined,
                   nodeTypes: js.UndefOr[js.Object] = js.undefined,
                   edgeTypes: js.UndefOr[js.Object] = js.undefined,
                   connectionLineType: js.UndefOr[String] = js.undefined,
                   connectionLineStyle: js.UndefOr[js.Dynamic] = js.undefined,
                   connectionLineComponent: js.UndefOr[ReactElement] = js.undefined,
                   deleteKeyCode: js.UndefOr[Int] = js.undefined,
                   selectionKeyCode: js.UndefOr[Int] = js.undefined,
                   snapToGrid: js.UndefOr[Boolean] = js.undefined,
                   snapGrid: js.UndefOr[(Int, Int)] = js.undefined,
                   onlyRenderVisibleNodes: js.UndefOr[Boolean] = js.undefined,
                   nodesDraggable: js.UndefOr[Boolean] = js.undefined,
                   nodesConnectable: js.UndefOr[Boolean] = js.undefined,
                   elementsRemovable: js.UndefOr[Boolean] = js.undefined,
                   elementsSelectable: js.UndefOr[Boolean] = js.undefined,
                   selectNodesOnDrag: js.UndefOr[Boolean] = js.undefined,
                   paneMoveable: js.UndefOr[Boolean] = js.undefined,
                   minZoom: js.UndefOr[Int] = js.undefined,
                   maxZoom: js.UndefOr[Int] = js.undefined,
                   defaultZoom: js.UndefOr[Int] = js.undefined,
                   defaultPosition: js.UndefOr[(Int, Int)] = js.undefined,
                   arrowHeadColor: js.UndefOr[String] = js.undefined,
                   markerEndId: js.UndefOr[String] = js.undefined,
                   zoomOnScroll: js.UndefOr[Boolean] = js.undefined,
                   zoomOnDoubleClick: js.UndefOr[Boolean] = js.undefined)

  override val component = ReactFlow
}