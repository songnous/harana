package com.harana.designer.frontend.schedules.list

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.common.grid.GridHandler
import com.harana.designer.frontend.common.grid.GridStore.{EntitySubType, UpdateAdditionalState, UpdateEditParameters, UpdateViewMode}
import com.harana.designer.frontend.common.grid.ui.GridPageItem
import com.harana.designer.frontend.common.ui.ViewMode
import com.harana.designer.frontend.schedules.list.ScheduleListStore._
import com.harana.designer.frontend.utils.ColorUtils
import com.harana.designer.frontend.utils.http.Http
import com.harana.designer.frontend.{Main, State}
import com.harana.sdk.shared.models.common.{Background, Visibility}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, StringParameter}
import com.harana.sdk.shared.models.schedules.{Action, Event, EventMode, Schedule, ScheduleExecution}
import com.harana.sdk.shared.utils.HMap
import com.harana.ui.components.LinkType
import diode._

import scala.collection.mutable.ListBuffer
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

class ScheduleListHandler extends GridHandler[Schedule, ScheduleListStore.State]("schedules", zoomTo(_.scheduleListState)) {

  override def gridHandle = Some({

    case AddEvent(event) =>
      val events = state.value.additionalState.itemEvents
      events += event
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemEvents = events))))

    case DeleteEvent(index) =>
      val events = state.value.additionalState.itemEvents
      events.remove(index)
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemEvents = events))))

    case UpdateEvent(index, newEvent) =>
      val events = state.value.additionalState.itemEvents
      events.update(index, newEvent)
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemEvents = events))))

    case AddAction(action) =>
      val actions = state.value.additionalState.itemActions
      actions += action
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemActions = actions))))

    case DeleteAction(index) =>
      val actions = state.value.additionalState.itemActions
      actions.remove(index)
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemActions = actions))))

    case UpdateAction(index, newAction) =>
      val actions = state.value.additionalState.itemActions
      actions.update(index, newAction)
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemActions = actions))))

    case UpdateActionTypes(actionTypes: List[String]) =>
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(actionTypes = actionTypes))))

    case UpdateEventTypes(eventTypes: List[String]) =>
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(eventTypes = eventTypes))))

    case UpdateItem(item: Option[Schedule]) =>
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(item = item))))

    case UpdateItemActions(actions: ListBuffer[Action]) =>
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemActions = actions))))

    case UpdateItemEvents(events: ListBuffer[Event]) =>
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemEvents = events))))

    case UpdateScheduleHistory(scheduleHistory) =>
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(scheduleHistory = scheduleHistory))))

  })


  def toGridPageItem(schedule: Schedule) =
    GridPageItem(
      id = schedule.id,
      title = schedule.title,
      description = Some(schedule.description),
      tags = schedule.tags,
      created = schedule.created,
      updated = schedule.updated,
      link = None,
      background = schedule.background,
      parameterValues = HMap[Parameter.Values](
        (GridPageItem.titleParameter, schedule.title),
        (GridPageItem.descriptionParameter, schedule.description),
        (GridPageItem.tagsParameter, schedule.tags)
      ),
      additionalData = Map(
        "actions" -> schedule.actions,
        "events" -> schedule.events,
        "recentExecutions" -> schedule.recentExecutions
      )
    )


  def toEntity(editedItem: Option[Schedule], subType: Option[EntitySubType], values: HMap[Parameter.Values]) =
    editedItem
      .getOrElse(
        Schedule(
          title = "",
          description = "",
          events = List(),
          eventMode = EventMode.All,
          actions = List(),
          successNotifiers = List(),
          errorNotifiers = List(),
          createdBy = Some(Main.claims.userId),
          visibility = Visibility.Owner,
          background = Some(Background.Image(ColorUtils.randomBackground)),
          tags = Set()
        )
      )
      .copy(
        title = values.getOrElse(GridPageItem.titleParameter, ""),
        description = values.getOrElse(GridPageItem.descriptionParameter, ""),
        tags = values.getOrElse(GridPageItem.tagsParameter, Set.empty[String])
      )


  override def onInit(preferences: Map[String, String]) =
    Some {
      val actions = if (state.value.additionalState.item.isEmpty) List(Action.DataSync()) else state.value.additionalState.item.get.actions
      val events = if (state.value.additionalState.item.isEmpty) List(Event.CalendarInterval()) else state.value.additionalState.item.get.events

      Effect(Http.getRelativeAs[List[String]](s"/api/schedules/actionTypes").map(at => UpdateActionTypes(at.getOrElse(List())))) +
      Effect(Http.getRelativeAs[List[String]](s"/api/schedules/eventTypes").map(et => UpdateEventTypes(et.getOrElse(List())))) +
      Effect(Http.getRelativeAs[List[(ScheduleExecution, Schedule)]](s"/api/schedules/history/20").map(et => UpdateScheduleHistory(et.getOrElse(List())))) +
      Effect.action(UpdateViewMode("schedules", ViewMode.List)) +
      Effect.action(UpdateEditParameters("schedules", List(
        ParameterGroup("about",
          StringParameter("title", required = true),
          StringParameter("description", multiLine = true, required = true)
        )))
      ) +
      Effect.action(UpdateItemActions(ListBuffer.from(actions))) +
      Effect.action(UpdateItemEvents(ListBuffer.from(events)))
    }


  override def onCreate(subType: Option[EntitySubType]) = {
    Analytics.scheduleCreate()
    None
  }


  override def onDelete(subType: Option[EntitySubType]) = {
    Analytics.scheduleDelete()
    None
  }

}