package com.harana.ui.external.d3_graph

import com.harana.ui.external.d3_graph.Types.NodeId
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-d3-graph", "Graph")
@js.native
object ReactD3Graph extends js.Object

@react object D3Graph extends ExternalComponent {

  case class Props(data: Data,
                   config: Option[Config] = None,
                   onClickGraph: Option[() => Unit] = None,
                   onClickNode: Option[NodeId => Unit] = None,
                   onRightClickNode: Option[NodeId => Unit] = None,
                   onMouseOverNode: Option[NodeId => Unit] = None,
                   onMouseOutNode: Option[NodeId => Unit] = None,
                   onClickLink: Option[(NodeId, NodeId) => Unit] = None,
                   onRightClickLink: Option[(NodeId, NodeId) => Unit] = None,
                   onMouseOverLink: Option[(NodeId, NodeId) => Unit] = None,
                   onMouseOutLink: Option[(NodeId, NodeId) => Unit] = None)

  override val component = ReactD3Graph
}

object Types {
  type NodeId = String
}

case class Data(nodes: List[Node], links: List[Link], focusedNodeId: Option[NodeId] = None)

case class Node(id: NodeId)

case class Link(sourceNodeId: NodeId, targetNodeId: NodeId)

case class Config(automaticRearrangeAfterDropNode: Option[Boolean] = None,
                  collapsible: Option[Boolean] = None,
                  directed: Option[Boolean] = None,
                  focusZoom: Option[Int] = None,
                  focusAnimationDuration: Option[Double] = None,
                  height: Option[Int] = None,
                  nodeHighlightBehavior: Option[Boolean] = None,
                  linkHighlightBehavior: Option[Boolean] = None,
                  highlightDegree: Option[Int] = None,
                  highlightOpacity: Option[Double] = None,
                  maxZoom: Option[Int] = None,
                  minZoom: Option[Int] = None,
                  panAndZoom: Option[Boolean] = None,
                  staticGraph: Option[Boolean] = None,
                  width: Option[Int] = None,
                  d3: Option[D3] = None)

case class D3(alphaTarget: Option[Double] = None,
              gravity: Option[Int] = None,
              linkLength: Option[Int] = None,
              linkStrength: Option[Int] = None)

case class NodeStyle(color: Option[String] = None,
                     fontColor: Option[String] = None,
                     fontSize: Option[String] = None,
                     fontWeight: Option[String] = None,
                     highlightColor: Option[String] = None,
                     highlightFontSize: Option[Int] = None,
                     highlightFontWeight: Option[String] = None,
                     highlightStrokeColor: Option[String] = None,
                     highlightStrokeWidth: Option[String] = None,
                     labelProperty: Option[String | Node => String] = None,
                     mouseCursor: Option[String] = None,
                     opacity: Option[Double] = None,
                     renderLabel: Option[Boolean] = None,
                     size: Option[Int] = None,
                     strokeColor: Option[String] = None,
                     strokeWidth: Option[Double] = None,
                     svg: Option[String] = None,
                     symbolType: Option[String] = None,
                     viewGenerator: Option[Node => js.Object] = None)

case class LinkStyle(color: Option[String] = None,
                     fontColor: Option[String] = None,
                     fontSize: Option[String] = None,
                     fontWeight: Option[String] = None,
                     highlightColor: Option[String] = None,
                     highlightFontSize: Option[Int] = None,
                     highlightFontWeight: Option[String] = None,
                     labelProperty: Option[String | Node => String] = None,
                     mouseCursor: Option[String] = None,
                     opacity: Option[Double] = None,
                     renderLabel: Option[Boolean] = None,
                     semanticStrokeWidth: Option[Boolean] = None,
                     strokeWidth: Option[Double] = None,
                    `type`: Option[String] = None)