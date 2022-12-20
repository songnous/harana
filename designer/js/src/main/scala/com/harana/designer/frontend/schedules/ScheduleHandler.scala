package com.harana.designer.frontend.schedules

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.common.grid.GridHandler
import com.harana.designer.frontend.common.grid.GridStore.{EntitySubType, UpdateAdditionalState, UpdateEditParameters, UpdateViewMode}
import com.harana.designer.frontend.common.grid.ui.GridPageItem
import com.harana.designer.frontend.common.ui.ViewMode
import ScheduleStore._
import com.harana.designer.frontend.utils.ColorUtils
import com.harana.designer.frontend.utils.http.Http
import com.harana.designer.frontend.{Main, State}
import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.{Background, Visibility}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, StringParameter}
import com.harana.sdk.shared.models.schedules.Action.ActionId
import com.harana.sdk.shared.models.schedules.Event.EventId
import com.harana.sdk.shared.models.schedules.{Action, Event, EventMode, Schedule, ScheduleExecution}
import com.harana.sdk.shared.utils.{HMap, Random}
import com.harana.ui.components.LinkType
import diode._

import scala.collection.mutable.ListBuffer
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

class ScheduleHandler extends GridHandler[Schedule, ScheduleStore.State]("schedules", zoomTo(_.scheduleState)) {

  override def gridHandle = Some({

    case AddEvent(event) =>
      val events = state.value.additionalState.itemEvents :+ (Random.short, event)
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemEvents = events))))

    case DeleteEvent(index) =>
      val events = state.value.additionalState.itemEvents
      events.remove(index)
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemEvents = events))))

    case UpdateEvent(index, newEvent) =>
      val events = state.value.additionalState.itemEvents
      events.update(index, (Random.short, newEvent))
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemEvents = events))))

    case AddAction(action) =>
      val actions = state.value.additionalState.itemActions :+ (Random.short, action)
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemActions = actions))))

    case DeleteAction(index) =>
      val actions = state.value.additionalState.itemActions
      actions.remove(index)
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemActions = actions))))

    case UpdateAction(index, newAction) =>
      val actions = state.value.additionalState.itemActions
      actions.update(index, (Random.short, newAction))
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemActions = actions))))

    case UpdateHistory(history) =>
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(history = history))))

    case UpdateItemActions(actions: List[(ActionId, Action)]) =>
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemActions = ListBuffer.from(actions)))))

    case UpdateItemEvents(events: List[(EventId, Event)]) =>
      effectOnly(Effect.action(UpdateAdditionalState("schedules", state.value.additionalState.copy(itemEvents = ListBuffer.from(events)))))

  })


  def toGridPageItem(schedule: Schedule) =
    GridPageItem(
      id = schedule.id,
      title = schedule.title,
      description = Some(schedule.description),
      tags = schedule.tags,
      created = schedule.created,
      updated = schedule.updated,
      entity = schedule,
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
        "recentExecutions" -> schedule.executions
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
        tags = values.getOrElse(GridPageItem.tagsParameter, Set.empty[String]),
        actions = value.additionalState.itemActions.toList,
        events = value.additionalState.itemEvents.toList
      )


  override def onInit(preferences: Map[String, String]) =
    Some {
      Effect.action(UpdateViewMode("schedules", ViewMode.List)) >>
      Effect.action(UpdateEditParameters("schedules", List(
        ParameterGroup("about",
          StringParameter("title", required = true),
          StringParameter("description", multiLine = true, required = true)
        )))
      )
    }


  override def onEdit = {
    val schedule = state.value.selectedItem.map(_.entity)
    val actions = if (schedule.isEmpty || schedule.get.actions.isEmpty) List((Random.short, Action.DataSync())) else schedule.get.actions
    val events = if (schedule.isEmpty || schedule.get.events.isEmpty) List((Random.short, Event.CalendarInterval())) else schedule.get.events
    Some(Effect.action(UpdateItemActions(actions)) >> Effect.action(UpdateItemEvents(events)))
  }


  override def onUpdateEntities(entities: List[Schedule]) = {
    val history = entities
      .flatMap(_.executions)
      .sortBy(_.started)
      .take(30)
      .map(se => (se, entities.find(_.id == se.scheduleId).get))

    Some(Effect.action(UpdateHistory(history)))
  }


  override def onSave(subType: Option[EntitySubType]) = {
    Analytics.scheduleCreate()
    None
  }


  override def onDelete(subType: Option[EntitySubType]) = {
    Analytics.scheduleDelete()
    None
  }

}