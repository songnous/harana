package com.harana.designer.frontend.flows.item.ui

import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.ParameterValue
import com.harana.sdk.backend.models.flow.Action.ActionId
import com.harana.sdk.backend.models.flow.ActionType
import com.harana.sdk.backend.models.flow.execution.ExecutionStatus
import com.harana.ui.external.flow.types.Position
import com.harana.ui.external.shoelace.ProgressBar
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html._

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal

@react object ActionNode {

  case class Props(id: String,
                   data: ActionNodeData,
                   `type`: String,
                   isConnectable: Boolean,
                   selected: Boolean,
                   sourcePosition: Option[Position],
                   targetPosition: Option[Position])

  val component = FunctionalComponent[Props] { props =>
    val actionType = props.data.actionType
    val title = props.data.title.getOrElse(i"actiontypes.${actionType.getClass.getSimpleName.toLowerCase}")
    val progressClass = props.data.executionStatus match {
      case ExecutionStatus.Executing => "newflow-item-progress-executing"
      case ExecutionStatus.Failed => "newflow-item-progress-failed"
      case ExecutionStatus.Cancelled => "newflow-item-progress-cancelled"
      case _ => ""
    }

    Fragment(
      handles(actionType.inputPorts, "source", left = true, vertical = props.data.vertical),
      div(className := "newflow-item", style := literal(backgroundColor = props.data.overrideColor.get))(
        div(className := "newflow-item-top")(title),
        div(className := "newflow-item-bottom")(
          ProgressBar(
            className = Some(s"newflow-item-progress $progressClass"),
            indeterminate = Some(props.data.executionStatus.equals(ExecutionStatus.Executing)),
            value = Some(props.data.percentage)
          )
        )
      ),
      handles(actionType.outputPorts, "target", left = false, vertical = props.data.vertical)
    )
  }
}

trait ActionNodeData extends js.Object {
  val id: ActionId
  val actionType: ActionType
  val parameterValues: Map[ParameterName, ParameterValue]
  val title: Option[String]
  val description: Option[String]
  val overrideColor: Option[String]
  val percentage: Int
  val executionStatus: ExecutionStatus
  val vertical: Boolean
}