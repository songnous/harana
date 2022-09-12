package com.harana.designer.frontend.schedules.list.ui.actions

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.common.grid.GridStore.UpdateNewOrEditDialog
import com.harana.designer.frontend.schedules.list.ScheduleListStore.{AddAction, DeleteAction, UpdateAction}
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.sdk.backend.models.designer.flow.Flow
import com.harana.sdk.shared.models.data.DataSource
import com.harana.sdk.shared.models.schedules.Action._
import com.harana.sdk.shared.models.schedules.{Action, Schedule}
import com.harana.ui.external.shoelace.{Button, ButtonGroup, MenuItem, Select}
import diode.ActionBatch
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
      td(
        Select(
          hoist = Some(true),
          name = s"${getClass.getSimpleName}-${props.rowIndex}",
          value = Some(props.action.map(_.getClass.getSimpleName).getOrElse("")),
          onChange = Some(v => Circuit.dispatch(UpdateAction(props.rowIndex, Action.withName(v)))),
          placeholder = Some("Select .."),
          options = Action.types.map(a => MenuItem(i"schedules.actions.${a.toLowerCase}", value=Some(a))),
          size = Some("large")
        )
      ),
      props.action.map {
        case a @ Action.DataSync(_) => DataSyncEditor(props.rowIndex, a, props.datasources)
        case a @ Action.FileCompress(_, _) => FileEditor(props.rowIndex, a)
        case a @ Action.FileCopy(_, _, _, _) => FileEditor(props.rowIndex, a)
        case a @ Action.FileDecompress(_, _) => FileEditor(props.rowIndex, a)
        case a @ Action.FileDelete(_, _) => FileEditor(props.rowIndex, a)
        case a @ Action.FileDuplicate(_, _) => FileEditor(props.rowIndex, a)
        case a @ Action.FileMkDir(_, _) => FileEditor(props.rowIndex, a)
        case a @ Action.FileMove(_, _, _, _) => FileEditor(props.rowIndex, a)
        case a @ Action.FileRename(_, _, _) => FileEditor(props.rowIndex, a)
        case a @ Action.FlowStart(_) => FlowEditor(props.rowIndex, a, props.flows)
        case a @ Action.FlowStop(_) => FlowEditor(props.rowIndex, a, props.flows)
        case a @ Action.HttpRequest(_, _, _, _) => HttpRequestEditor(props.rowIndex, a)
        case a @ Action.ScheduleEnable(_) => ScheduleEditor(props.rowIndex, a, props.schedules)
        case a @ Action.ScheduleDisable(_) => ScheduleEditor(props.rowIndex, a, props.schedules)
      },
      td(
        ButtonGroup(label = Some("Buttons"))(
          List(
            Button(icon = Some(("icomoon", "plus3")), circle = Some(true), size = Some("small"), onClick = Some(_ =>
              Circuit.dispatch(AddAction(Action.DataSync()))
            )),
            Button(icon = Some(("icomoon", "minus3")), circle = Some(true), size = Some("small"), disabled = Some(!props.allowDelete), onClick = Some(_ =>
              Circuit.dispatch(DeleteAction(props.rowIndex))
            ))
          )
        )
      )
    )
  }
}