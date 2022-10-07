package com.harana.designer.frontend.flows.item

import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.sdk.shared.models.flow.{Action, ActionTypeInfo, Catalog}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory
import com.harana.sdk.shared.models.flow.execution.spark.ExecutionStatus
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.graph.{Edge, Endpoint}
import com.harana.sdk.shared.utils.Random
import com.harana.ui.external.flow.types.{FlowElement, FlowNode, HandleType}
import com.harana.ui.external.flow.{Connection, FlowEdge, Handle, XYPosition}
import enumeratum._
import slinky.core.ReactComponentClass
import slinky.core.facade.ReactElement

import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}
import scala.reflect.runtime.universe.TypeTag
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
  def actionTypeColour(info: ActionTypeInfo) =
    info.category match {
      case ActionCategory.Action => "#beabca"
      case ActionCategory.Filtering => "#caabc2"
      case ActionCategory.IO => "#cac4ab"
      case ActionCategory.ML => "#caabb9"
      case ActionCategory.Transformation => "#abcabc"
    }


  @inline
  def pill(color: String) = {
   import slinky.web.svg._
   svg(className := "flow-component-pill", width := "20px", height := "6.5px", version := "1.1")(
      rect(x := "0", y := "0", width := "20", height := "5", rx := "2", fill := color)
    )
  }


  @inline
  def isNode(element: FlowElement) =
    !isEdge(element)


  @inline
  def toNode(element: FlowElement): FlowNode =
    toNode(element.asInstanceOf[FlowNode])


  @inline
  def toNode[T <: ActionTypeInfo](node: FlowNode) =
    Node(node.id,
      Action[T](
        node.id,
        node.data.actionType.asInstanceOf[T],
        (node.position.x, node.position.y),
        node.data.actionType.inArity,
        node.data.actionType.outArity,
        node.data.title,
        node.data.description,
        node.data.overrideColor,
        node.data.parameterValues
      )
    )


  @inline
  def toFlowNode[T <: ActionTypeInfo](action: Action[T], i18nPrefix: String)(implicit ct: ClassTag[T]) = {
    new FlowNode {
      val id = action.id
      val position = new XYPosition {
        val x = action.position._1
        val y = action.position._2
      }
      val `type` = "actionNode"
      override val data = new ActionNodeData {
        val id = action.id
        val title = io"$i18nPrefix.${action.id}.title"
        val description = io"$i18nPrefix.${action.id}.description"
        val overrideColor = action.overrideColor
        val percentage = 0
        val executionStatus = ExecutionStatus.None
        val vertical = false
        val actionType = Catalog.actionsMap(ct.runtimeClass.asInstanceOf[ActionTypeInfo].id)
        val parameterValues = action.parameterValues
      }
    }
  }


  @inline
  def toEdge(element: FlowElement): Edge =
    toEdge(element.asInstanceOf[FlowEdge])


  @inline
  def toEdge(flowEdge: FlowEdge): Edge = {
    Edge(Endpoint(flowEdge.source, flowEdge.sourceHandle.toInt), Endpoint(flowEdge.target, flowEdge.targetHandle.toInt))
  }


  @inline
  def toFlowEdge(edge: Edge): FlowEdge =
    new FlowEdge {
      val id = Random.long
      val source = edge.from.nodeId.toString
      val sourceHandle = edge.from.portIndex.toString
      val target = edge.to.nodeId.toString
      val targetHandle = edge.to.portIndex.toString
    }

  @inline
  def isEdge(element: FlowElement) =
    js.Object.hasProperty(element.asInstanceOf[js.Object], "source")


  @inline
  def handles(ports: List[ru.TypeTag[_]], handleType: HandleType, left: Boolean, vertical: Boolean): List[ReactElement] =
    ports.zipWithIndex.map { case (port, index) =>
      val style = handleStyle(ports.size, vertical)(index)
      Handle(
        handleType, 
        handlePosition(left, vertical),
        id = s"$index",
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