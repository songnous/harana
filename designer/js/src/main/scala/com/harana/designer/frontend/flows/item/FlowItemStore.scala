package com.harana.designer.frontend.flows.item

import com.harana.designer.frontend.flows.item.ui.FlowTab
import com.harana.designer.frontend.utils.UndoHistory
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.ParameterValue
import com.harana.sdk.shared.models.flow.Flow.FlowId
import com.harana.sdk.shared.models.flow.Action.ActionId
import com.harana.sdk.shared.models.flow.{Action, Flow, FlowExecution}
import com.harana.sdk.shared.utils.Random
import com.harana.ui.external.flow.types.FlowElement
import com.harana.ui.external.flow.{Connection, Edge, FlowInstance, Node}
import diode.{Action => DiodeAction}
import slinky.web.SyntheticMouseEvent

object FlowItemStore {

  type UndoState = (List[Node], List[Edge])

  case class FlowItemState(flow: Option[Flow],
                           flowExecution: Option[FlowExecution],
                           isDirty: Boolean,
                           isRunning: Boolean,
                           isEditingParameters: Boolean,
                           flowInstance: Option[FlowInstance],
                           logsKey: String,
                           nodes: List[Node],
                           edges: List[Edge],
                           actionTypes: List[ActionType],
                           activeActionIds: List[ActionId],
                           completedActionIds: List[ActionId],
                           selectedActionId: Option[ActionId],
                           selectedActionType: Option[ActionType],
                           selectedTab: FlowTab,
                           portsOrientation: Boolean,
                           showLogs: Boolean,
                           showMiniMap: Boolean,
                           showGrid: Boolean,
                           snapToGrid: Boolean,
                           undoHistory: UndoHistory[UndoState])

  val initialState = FlowItemState(None, None, false, false, false, None, Random.short, List(), List(), List(), List(), List(), None, None, FlowTab.ActionTypes, false, false, true, true, true, new UndoHistory[UndoState])


  case object Reset extends DiodeAction
  case class Init(userPreferences: Map[String, String]) extends DiodeAction
  case class ReceiveEvent(message: String, payload: String) extends DiodeAction

  case class OpenFlow(id: FlowId) extends DiodeAction

  case object StartFlow extends DiodeAction
  case object StopFlow extends DiodeAction
  case object SaveFlow extends DiodeAction
  case object DirtyFlow extends DiodeAction

  case class SelectTab(tab: FlowTab) extends DiodeAction

  case object ZoomIn extends DiodeAction
  case object ZoomOut extends DiodeAction
  case object ZoomToFit extends DiodeAction

  case object Undo extends DiodeAction
  case object Redo extends DiodeAction

  case class AddAction(event: SyntheticMouseEvent[_]) extends DiodeAction
  case class AddConnection(connection: Connection) extends DiodeAction
  case class RunAction(action: Action) extends DiodeAction
  case class SelectAction(actionId: ActionId) extends DiodeAction
  case class SelectActionType(actionType: ActionType) extends DiodeAction
  case object DeselectAllActions extends DiodeAction

  case class DeleteElements(elements: List[FlowElement]) extends DiodeAction

  case class UpdateActionTypes(actionTypes: List[ActionType]) extends DiodeAction
  case class UpdateFlow(flow: Flow) extends DiodeAction
  case class UpdateFlowExecution(flowExecution: FlowExecution) extends DiodeAction
  case class UpdateFlowInstance(instance: FlowInstance) extends DiodeAction
  case class UpdateIsEditingParameters(editing: Boolean) extends DiodeAction
  case class UpdateNode(node: Node) extends DiodeAction
  case class UpdateParameterValues(actionId: ActionId, parameterValues: Map[ParameterName, ParameterValue]) extends DiodeAction
  case class UpdatePercentage(node: Node, percentage: Int) extends DiodeAction
  case class UpdatePortsOrientation(vertical: Boolean) extends DiodeAction

  case object ToggleLogs extends DiodeAction
  case object ToggleShowGrid extends DiodeAction
  case object ToggleSnapToGrid extends DiodeAction
  case object ToggleShowMiniMap extends DiodeAction
}