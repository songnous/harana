package com.harana.designer.frontend.flows.item

import com.harana.sdk.shared.models.designer.flow.execution.ExecutionStatus
import com.harana.sdk.shared.models.flow.Action
import com.harana.sdk.shared.utils.Random
import com.harana.ui.external.flow.types.{FlowElement, HandleType}
import com.harana.ui.external.flow.{Connection, Edge, Node, XYPosition}
import enumeratum._
import slinky.core.ReactComponentClass
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal

package object ui {

  trait NodeTypes extends js.Object {
    val actionNode: ReactComponentClass[_]
  }

  sealed trait FlowTab extends EnumEntry
  object FlowTab extends Enum[FlowTab] {
    case object ActionTypes extends FlowTab
    case object DataSources extends FlowTab
    case object Parameters extends FlowTab
    case object Run extends FlowTab
    val values = findValues
  }


  @inline
  def actionTypeColour(info: ActionType) =
    info.group match {
      case "info" => "#beabca"
      case "input" => "#caabc2"
      case "output" => "#abcabc"
      case "query" => "#abb9ca"
      case "search" => "#caabb9"
      case "transform" => "#cac4ab"
    }


  @inline
  def pill(color: String) = {
   import slinky.web.svg._
   svg(className := "flow-component-pill", width := "20px", height := "6.5px", version := "1.1")(
      rect(x := "0", y := "0", width := "20", height := "5", rx := "2", fill := color)
    )
  }


  @inline
  def data(node: Node) =
    node.data.asInstanceOf[ActionNodeData]


  @inline
  def toAction(node: Node) = {
    val d = data(node)
    Action(node.id, d.actionType, (node.position.x, node.position.y), d.title, d.description, d.overrideColor, d.parameterValues)
  }


  @inline
  def toNode(action: Action) =
    new Node {
      val id = action.id
      val position = new XYPosition {
        val x = action.position._1
        val y = action.position._2
      }
      val `type` = "actionNode"
      override val data = new ActionNodeData {
        val id = action.id
        val actionType = action.actionType
        val parameterValues = action.parameterValues
        val title = action.title
        val description = action.description
        val overrideColor = action.overrideColor
        val percentage = 0
        val executionStatus = ExecutionStatus.None
        val vertical = false
      }
    }


  @inline
  def toLink(edge: Edge) =
    Link(edge.target, Port.DataFrame(edge.targetHandle), edge.source, Port.DataFrame(edge.sourceHandle))


  @inline
  def toEdge(link: Link) =
    new Edge {
      val id = Random.long
      val source = link.toAction
      val sourceHandle = link.toPort.name
      val target = link.fromAction
      val targetHandle = link.fromPort.name
    }


  @inline
  def toEdge(element: FlowElement) =
    element.asInstanceOf[Edge]


  @inline
  def toNode(element: FlowElement) =
    element.asInstanceOf[Node]


  @inline
  def isEdge(element: FlowElement) =
    js.Object.hasProperty(element.asInstanceOf[js.Object], "source")


  @inline
  def isNode(element: FlowElement) =
    !isEdge(element)


  @inline
  def handles(ports: List[Port], handleType: HandleType, left: Boolean, vertical: Boolean): List[ReactElement] =
    ports.zipWithIndex.map { case (port, index) =>
      val style = handleStyle(ports.size, vertical)(index)
      Handle(
        handleType, 
        handlePosition(left, vertical),
        id = s"${port.name}",
        style = style, 
        className = "newflow-item-handle", 
        isValidConnection = (connection: Connection) => true
      )
    }


  @inline
  def isSource(handleId: String) =
    handleId.substring(handleId.indexOf("#") + 1, handleId.length).equals("source")


  @inline
  def handlePosition(left: Boolean, vertical: Boolean) =
    (left, vertical) match {
      case (true, true)     => "top"
      case (true, false)    => "left"
      case (false, true)    => "bottom"
      case (false, false)   => "right"
    }


  @inline
  def handleStyle(portCount: Int, vertical: Boolean) =
    (portCount, vertical) match {
      case (1, true)   =>  literal() :: Nil
      case (1, false)  =>  literal(top = "22px") :: Nil
      case (2, true)   =>  literal() :: literal() :: Nil
      case (2, false)  =>  literal(top = "10px") :: literal(top = "auto", bottom = "18px") :: Nil
      case _           =>  literal() :: Nil
    }
}