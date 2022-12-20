package com.harana.designer.frontend.schedules.ui.actions

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.schedules.ScheduleStore.{AddAction, DeleteAction, UpdateAction}
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.sdk.shared.models.data.DataSource
import com.harana.sdk.shared.models.flow.Flow
import com.harana.sdk.shared.models.schedules.{Action, Schedule}
import com.harana.ui.external.shoelace.{Button, ButtonGroup, MenuItem, Select}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html._

@react object ActionRow {

  case class Props(rowIndex: Int,
                   action: Option[Action] = None,
                   datasources: List[DataSource],
                   flows: List[Flow],
                   schedules: List[Schedule],
                   allowDelete: Boolean = true)

  val component = FunctionalComponent[Props] { props =>
    tr(
      td(className := "schedule-row-type")(
        Select(
          hoist = Some(true),
          name = s"${getClass.getSimpleName}-${props.rowIndex}",
          value = Some(props.action.map(_.getClass.getSimpleName).getOrElse("")),
          onChange = Some(v => Circuit.dispatch(UpdateAction(props.rowIndex, Action.newWithName(v)))),
          placeholder = Some("Select .."),
          options = Action.typesByName.map(a => MenuItem(i"schedules.actions.${a.toLowerCase}", value=Some(a))),
          size = Some("large")
        )
      ),
      props.action.map {
        case a @ Action.DataSync(_)               => DataSyncEditor(props.rowIndex, a, props.datasources)
        case a @ Action.ExecuteCommand(_)         => ExecuteCommandEditor(props.rowIndex, a)
        case a @ Action.ExecuteScript(_)          => ExecuteScriptEditor(props.rowIndex, a)
        case a @ Action.FlowStart(_)              => FlowEditor(props.rowIndex, a, props.flows)
        case a @ Action.FlowStop(_)               => FlowEditor(props.rowIndex, a, props.flows)
        case a @ Action.HttpRequest(_, _, _, _)   => HttpRequestEditor(props.rowIndex, a)
        case a @ Action.ScheduleEnable(_)         => ScheduleEditor(props.rowIndex, a, props.schedules)
        case a @ Action.ScheduleDisable(_)        => ScheduleEditor(props.rowIndex, a, props.schedules)
        case a @ Action.ScheduleTrigger(_)        => ScheduleEditor(props.rowIndex, a, props.schedules)
        case a @ Action.SendEmail(_)              => SendEmailEditor(props.rowIndex, a)
        case a @ Action.SendSlackMessage(_)       => SendSlackMessageEditor(props.rowIndex, a)
      },
      td(className := "schedule-row-actions")(
        ButtonGroup(label = Some("Buttons"))(
          List(
            Button(icon = Some(("icomoon", "plus3")), iconClassName = Some("schedule-action-button"), circle = Some(true), size = Some("small"), onClick = Some(_ =>
              Circuit.dispatch(AddAction(Action.DataSync()))
            )),
            Button(icon = Some(("icomoon", "minus3")), iconClassName = Some("schedule-action-button"), circle = Some(true), size = Some("small"), disabled = Some(!props.allowDelete), onClick = Some(_ =>
              Circuit.dispatch(DeleteAction(props.rowIndex))
            ))
          )
        )
      )
    )
  }
}