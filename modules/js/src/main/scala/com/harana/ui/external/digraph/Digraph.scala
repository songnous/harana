package com.harana.ui.external.digraph

import com.harana.ui.external.digraph.Types._
import org.scalajs.dom.HTMLDivElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-digraph", "GraphView")
@js.native
object ReactGraphView extends js.Object

@react object GraphView extends ExternalComponent {

  case class Props(nodes: List[ReactElement],
                   edges: List[ReactElement],
                   minZoom: Option[Int] = None,
                   maxZoom: Option[Int] = None,
                   readOnly: Option[Boolean] = None,
                   maxTitleChars: Option[Int] = None,
                   nodeSize: Option[Int] = None,
                   edgeHandleSize: Option[Int] = None,
                   edgeArrowSize: Option[Int] = None,
                   zoomDelay: Option[Int] = None,
                   zoomDur: Option[Int] = None,
                   showGraphControls: Option[Boolean] = None,
                   nodeKey: String,
                   gridSize: Option[Int] = None,
                   gridSpacing: Option[Int] = None,
                   gridDotSize: Option[Int] = None,
                   backgroundFillId: Option[String] = None,
                   nodeTypes: js.Any,
                   nodeSubtypes: js.Any,
                   edgeTypes: js.Any,
                   selected: js.Any,
                   onDeleteNode: (js.Any, String, List[js.Any]) => Unit,
                   onSelectNode: (Node | Null) => Unit,
                   onCreateNode: (Int, Int, js.Object) => Unit,
                   onCreateEdge: (Node, Node) => Unit,
                   onDeleteEdge: (Edge, List[Edge]) => Unit,
                   onUpdateNode: Node => Unit,
                   onSwapEdge: (Node, Node, Edge) => Unit,
                   onSelectEdge: Edge => Unit,
                   canDeleteNode: Option[js.Any => Boolean] = None,
                   canDeleteEdge: Option[js.Any => Boolean] = None,
                   canCreateEdge: Option[(Node, Node) => Boolean] = None,
                   afterRenderEdge: Option[(String, js.Any, Edge, js.Any, Boolean) => Unit] = None,
                   onUndo: Option[() => Unit] = None,
                   onCopySelected: Option[() => Unit] = None,
                   onPasteSelected: Option[() => Unit] = None,
                   renderBackground: Option[Int => js.Any] = None,
                   renderDefs: Option[() => js.Any] = None,
                   renderNode: Option[(js.Any, js.Any, Int, Boolean, Boolean) => js.Any] = None,
                   renderNodeText: Option[(js.Any, String | Int, Boolean) => js.Any] = None,
                   layoutEngineType: Option[String] = None)

  override val component = ReactGraphView
}

@JSImport("react-digraph", "Node")
@js.native
object ReactNode extends js.Object

@react object Node extends ExternalComponent {

  case class Props(data: INode,
                   id: String,
                   nodeTypes: js.Any,
                   nodeSubtypes: js.Any,
                   opacity: Option[Int] = None,
                   nodeKey: String,
                   nodeSize: Option[Int] = None,
                   onNodeMouseEnter: (js.Any, js.Any, Boolean) => Unit,
                   onNodeMouseLeave: (js.Any, js.Any) => Unit,
                   onNodeMove: (IPoint, String, Boolean) => Unit,
                   onNodeSelected: (js.Any, String, Boolean) => Unit,
                   onNodeUpdate: (IPoint, String, Boolean) => Unit,
                   renderNode: Option[(js.Any, js.Any, String, Boolean, Boolean) => js.Any] = None,
                   renderNodeText: Option[(js.Any, String | Int, Boolean) => js.Any] = None,
                   isSelected: Boolean,
                   layoutEngine: Option[js.Any] = None,
                   viewWrapperElem: HTMLDivElement)

  override val component = ReactNode
}

@JSImport("react-digraph", "Edge")
@js.native
object ReactEdge extends js.Object

@react object Edge extends ExternalComponent {

  case class Props(data: IEdge,
                   edgeTypes: js.Any,
                   edgeHandleSize: Option[Int] = None,
                   nodeSize: Option[Int] = None,
                   sourceNode: INode | Null,
                   targetNode: INode | ITargetPosition,
                   isSelected: Boolean,
                   nodeKey: String,
                   viewWrapperElem: HTMLDivElement)

  override val component = ReactEdge
}

case class INode(id: String,
                 title: String,
                 x: Option[Double],
                 y: Option[Double],
                 `type`: Option[String],
                 `subtype`: Option[String])

case class IEdge(source: String,
                 target: String,
                 `type`: Option[String],
                 handleText: Option[String])

case class IPoint(x: Int, y: Int)

case class ITargetPosition(x: Int, y: Int)

object Types {
  type Node = ReactElement
  type Edge = ReactElement
}