package com.harana.designer.frontend.flows.item

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.flows.item.FlowItemStore._
import com.harana.designer.frontend.flows.item.ui._
import com.harana.designer.frontend.utils.http.Http
import com.harana.designer.frontend.{Circuit, State}
import com.harana.sdk.shared.models.flow.catalog.Catalog
import com.harana.sdk.shared.models.flow.execution.spark.ExecutionStatus
import com.harana.sdk.shared.models.flow.graph.FlowGraph
import com.harana.sdk.shared.models.flow.{ActionTypeInfo, Flow, FlowExecution}
import com.harana.sdk.shared.utils.{HMap, Random}
import com.harana.ui.external.flow.types.FlowNode
import com.harana.ui.external.flow.{Connection, FlowEdge, XYPosition}
import diode.{ActionHandler, ActionResult, Effect, NoAction}
import io.circe.syntax.EncoderOps

import java.util.Timer
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.scalajs.js

class FlowItemHandler extends ActionHandler(zoomTo(_.flowItemState)) {
  override def handle: PartialFunction[Any, ActionResult[State]] = {

    case Reset =>
      updated(initialState)


    case Init(preferences) =>
      new Timer().scheduleAtFixedRate(new java.util.TimerTask {
        def run(): Unit = Circuit.dispatch(SaveFlow)
      }, 0L, 2000L)

      println("A")
      try {
        val actions = Catalog.actionsMap.values.toList
        println(s"Actions size = ${actions.size}")
      } catch {
        case e: Exception => e.printStackTrace()
      }
      println("B")
      effectOnly(Effect.action(UpdateActionTypes(List())))


    case ReceiveEvent(eventType, eventParameters) =>
      noChange


    case OpenFlow(id) =>
      effectOnly(
        Effect.action(Reset) +
        Effect(Http.getRelativeAs[Flow](s"/api/flows/$id").map(f => if (f.isDefined) UpdateFlow(f.get) else NoAction)) >>
        Effect(Http.getRelativeAs[FlowExecution](s"/api/execution/flows/progress/$id").map(fe => if (fe.isDefined) UpdateFlowExecution(fe.get) else NoAction))
      )


    case StartFlow =>
      updated(value.copy(nodes = value.nodes, isRunning = true, selectedTab = FlowTab.Run), Effect(
        Http.putRelativeAs[FlowExecution](s"/api/flows/start/${value.flow.map(_.id).getOrElse("")}", List(), "").map(fe =>
          if (fe.isDefined) {
            Analytics.flowStart(value.flow.get.id, value.flow.get.graph.nodes.size)
            UpdateFlowExecution(fe.get)
          } else NoAction)
      ))


    case StopFlow =>
      updated(value.copy(isRunning = false), Effect(
        Http.putRelativeAs[FlowExecution](s"/api/flows/stop/${value.flowExecution.map(_.id).getOrElse("")}", List(), "").map(fe =>
          if (fe.isDefined) {

            val stopCause = fe.get.executionStatus match {
              case ExecutionStatus.Cancelled => "cancelled"
              case ExecutionStatus.Failed => "failed"
              case ExecutionStatus.Killed => "killed"
              case ExecutionStatus.Succeeded => "succeeded"
              case ExecutionStatus.TimedOut => "timed_out"
              case _ => "unknown"
            }

            Analytics.flowStop(value.flow.get.id, fe.get.created, value.flow.get.graph.nodes.size, fe.get.acceptedTime, fe.get.endTime, stopCause)
            UpdateFlowExecution(fe.get)
          } else NoAction)
      ))


    case SelectTab(tab) =>
      updated(value.copy(selectedTab = tab))


    case ZoomIn =>
      value.flowInstance.get.zoomIn()
      noChange


    case ZoomOut =>
      value.flowInstance.get.zoomOut()
      noChange


    case Undo =>
      val change = value.undoHistory.undo()
      updated(value.copy(nodes = change._1, edges = change._2))


    case Redo =>
      val change = value.undoHistory.redo()
      updated(value.copy(nodes = change._1, edges = change._2))


    case AddAction(event) =>
      value.selectedActionType match {
        case Some(at) =>
          val actionId = Random.long
          val color = actionTypeColour(at)

          val nodeData = new ActionNodeData {
            val id = actionId
            val actionType = at
            val title = None
            val description = None
            val overrideColor = Some(color)
            val percentage = 0
            val executionStatus = ExecutionStatus.None
            val vertical = value.portsOrientation
            val parameterValues = HMap.empty
          }

          val node = new FlowNode {
            val id = actionId
            val `type` = "actionNode"
            val position = value.flowInstance.get.project(
              new XYPosition {
                val x = event.clientX.toInt
                val y = event.clientY.toInt
              })
            override val data = nodeData
          }

          value.undoHistory.push((value.nodes, value.edges))
          updated(value.copy(isDirty = true, nodes = value.nodes :+ node), Effect.action(SelectAction(actionId)))

        case None => noChange
      }


    case AddConnection(connection: Connection) =>
      if (connection.sourceHandle != null) {
        val sourceNode = value.nodes.find(_.id == connection.source).get

        val edge = new FlowEdge {
          val id = Random.short
          val source = connection.source
          val sourceHandle = connection.sourceHandle
          val target = connection.target
          val targetHandle = connection.targetHandle
          override val style = js.Dynamic.literal(strokeWidth = "2")
        }

        val updatedEdges = value.edges :+ edge
        value.undoHistory.push((value.nodes, updatedEdges))
        updated(value.copy(isDirty = true, edges = updatedEdges))
      } else noChange


    case SaveFlow =>
      if (value.flow.isDefined && value.isDirty) {
        val updatedFlow = value.flow.get.copy(graph = FlowGraph(
          value.nodes.map(toNode).toSet,
          value.edges.map(toEdge).toSet
        ))
        updated(
          value.copy(
            isDirty = false,
            flow = Some(updatedFlow),
            undoHistory = value.undoHistory
          ), Effect(Http.putRelative(s"/api/flows", List(), updatedFlow.asJson.noSpaces).map(_ => NoAction)))
      } else {
        noChange
      }


    case SelectAction(actionId) =>
      updated(value.copy(selectedActionId = Some(actionId), selectedTab = FlowTab.Parameters))


    case SelectActionType(actionType) =>
      updated(value.copy(selectedActionType = Some(actionType)))


    case DeselectAllActions =>
      updated(value.copy(selectedActionId = None, selectedTab = FlowTab.ActionTypes))


    case DeleteElements(elements) =>
      val actionIds = elements.filter(isNode).map(toNode(_).id)
      val edgeIds = elements.filter(isEdge).map(e => (toEdge(e).from.portIndex, toEdge(e).to.portIndex))

      val selectedActionId = if (value.selectedActionId.isDefined && actionIds.contains(value.selectedActionId.get)) None else value.selectedActionId
      val completedActionIds = value.completedActionIds.diff(actionIds)
      val activeActionIds = value.activeActionIds.diff(actionIds)

      val nodes = value.nodes.filterNot(node => actionIds.contains(node.id))
      val edges = value.edges
        .filterNot(edge => actionIds.contains(edge.source) || actionIds.contains(edge.target))
        .filterNot(edge => edgeIds.map(_._1).contains(edge.source) && edgeIds.map(_._2).contains(edge.target))

      value.undoHistory.push((nodes, edges))
      updated(value.copy(
        isDirty = true,
        selectedActionId = selectedActionId,
        completedActionIds = completedActionIds,
        activeActionIds = activeActionIds,
        nodes = nodes,
        edges = edges,
        selectedTab = FlowTab.ActionTypes)
      )


    case ToggleLogs =>
      updated(value.copy(showLogs = !value.showLogs))


    case ToggleShowGrid =>
      updated(value.copy(showGrid = !value.showGrid))


    case ToggleSnapToGrid =>
      updated(value.copy(snapToGrid = !value.snapToGrid))


    case ToggleShowMiniMap =>
      updated(value.copy(showMiniMap = !value.showMiniMap))


    case UpdateActionTypes(actionTypes) =>
      updated(value.copy(actionTypes = actionTypes))


    case UpdateFlow(flow) =>
      val nodes = flow.graph.nodes.map(n => toFlowNode(n.value, "")).toList
      val edges = flow.graph.edges.map(toFlowEdge).toList

      value.undoHistory.init((nodes, edges))
      updated(value.copy(flow = Some(flow), nodes = nodes, edges = edges))


    case UpdateFlowExecution(flowExecution) =>
      value.nodes.foreach { node =>
        flowExecution.actionExecutions.find(_.actionId.toString == node.id) match {
          case Some(fe) =>
            setExecutionStatus(node, fe.executionStatus)
            setPercentage(node, fe.percentage)

          case None =>
            setExecutionStatus(node, ExecutionStatus.None)
            setPercentage(node, 0)
        }
      }

      val isRunning = flowExecution.executionStatus match {
        case ExecutionStatus.Executing | ExecutionStatus.PendingExecution | ExecutionStatus.PendingCancellation => true
        case _ => false
      }

      updated(value.copy(flowExecution = Some(flowExecution), isRunning = isRunning, logsKey = Random.short, nodes = value.nodes))


    case UpdateFlowInstance(instance) =>
      updated(value.copy(flowInstance = Some(instance)))


    case UpdateIsEditingParameters(isEditing) =>
      updatedSilent(value.copy(isEditingParameters = isEditing))


    case UpdateNode(node) =>
      val updatedNodes = value.nodes.map(n => if (n.id == node.id) node else n)
      value.undoHistory.push((updatedNodes, value.edges))
      updated(value.copy(isDirty = true, nodes = updatedNodes))


    case UpdateParameterValues(actionId, values) =>
      val node = value.nodes.find(_.id == actionId.toString)
      if (node.isDefined) {
        val d = node.get.data
        val updatedData = new ActionNodeData {
          val id = node.get.id
          val actionType = d.actionType
          val parameterValues = values
          val title = d.title
          val description = d.description
          val overrideColor = d.overrideColor
          val percentage = d.percentage
          val executionStatus = d.executionStatus
          val vertical = d.vertical
        }
        node.get.asInstanceOf[js.Dynamic].updateDynamic("data")(updatedData)

        val updatedNodes = value.nodes.map(n => if (n.id == node.get.id) node.get else n)
        updated(value.copy(isDirty = true, nodes = updatedNodes))
      } else
        noChange


    case UpdatePortsOrientation(orientation) =>
      value.nodes.foreach(updateData(_, "vertical", orientation))
      updated(value.copy(nodes = value.nodes, edges = value.edges, portsOrientation = orientation))
  }

  private def updateData(node: FlowNode, key: String, value: js.Any) =
    node.data.asInstanceOf[js.Dynamic].updateDynamic(key)(value)

  private def setExecutionStatus(node: FlowNode, executionStatus: ExecutionStatus) =
    updateData(node, "executionStatus", executionStatus.asInstanceOf[js.Any])

  private def setPercentage(node: FlowNode, value: Int) =
    updateData(node, "percentage", value)
}