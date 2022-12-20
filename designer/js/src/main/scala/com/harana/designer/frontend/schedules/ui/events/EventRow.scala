package com.harana.designer.frontend.schedules.ui.events

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.designer.frontend.schedules.ScheduleStore._
import com.harana.sdk.shared.models.data.DataSource
import com.harana.sdk.shared.models.flow.Flow
import com.harana.sdk.shared.models.schedules.Event._
import com.harana.sdk.shared.models.schedules.{Event, Schedule}
import com.harana.ui.external.shoelace.{Button, ButtonGroup, MenuItem, Select}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html.{className, i, td, tr}

@react object EventRow {

  case class Props(rowIndex: Int,
                   event: Option[Event] = None,
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
          value = Some(props.event.map(_.getClass.getSimpleName).getOrElse("")),
          onChange = Some(v => Circuit.dispatch(UpdateEvent(props.rowIndex, Event.newWithName(v)))),
          placeholder = Some("Select .."),
          options = Event.typesByName.map(a => MenuItem(i"schedules.events.${a.toLowerCase}", value=Some(a))),
          size = Some("large")
        )
      ),
      props.event.map {
        case e @ Event.CalendarInterval(_, _, _)  => CalendarIntervalEditor(props.rowIndex, e)
        case e @ Event.CalendarSchedule(_, _, _)  => CalendarScheduleEditor(props.rowIndex, e)
        case e @ Event.DataSyncStarted(_)         => DataSyncEditor(props.rowIndex, e, props.datasources)
        case e @ Event.DataSyncFinished(_)        => DataSyncEditor(props.rowIndex, e, props.datasources)
        case e @ Event.DataSyncFailed(_, _)       => DataSyncEditor(props.rowIndex, e, props.datasources)
        case e @ Event.FileCreated(_, _)          => FileEditor(props.rowIndex, e)
        case e @ Event.FileModified(_, _)         => FileEditor(props.rowIndex, e)
        case e @ Event.FileDeleted(_, _)          => FileEditor(props.rowIndex, e)
        case e @ Event.FlowStarted(_)             => FlowEditor(props.rowIndex, e, props.flows)
        case e @ Event.FlowFinished(_)            => FlowEditor(props.rowIndex, e, props.flows)
        case e @ Event.FlowFailed(_, _)           => FlowEditor(props.rowIndex, e, props.flows)
        case e @ Event.ScheduleStarted(_)         => ScheduleEditor(props.rowIndex, e, props.schedules)
        case e @ Event.ScheduleFinished(_)        => ScheduleEditor(props.rowIndex, e, props.schedules)
        case e @ Event.ScheduleFailed(_, _)       => ScheduleEditor(props.rowIndex, e, props.schedules)
      },
      td(className := "schedule-row-actions")(
        ButtonGroup(label = Some("Buttons"))(
          List(
            Button(icon = Some(("icomoon", "plus3")), iconClassName = Some("schedule-action-button"), circle = Some(true), size = Some("small"), onClick = Some(_ =>
              Circuit.dispatch(AddEvent(Event.CalendarInterval()))
            )),
            Button(icon = Some(("icomoon", "minus3")), iconClassName = Some("schedule-action-button"), circle = Some(true), size = Some("small"), disabled = Some(!props.allowDelete), onClick = Some(_ =>
              Circuit.dispatch(DeleteEvent(props.rowIndex))
            ))
          )
        )
      )
    )
  }
}