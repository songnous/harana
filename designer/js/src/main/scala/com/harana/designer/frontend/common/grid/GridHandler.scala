package com.harana.designer.frontend.common.grid

import com.harana.designer.frontend.State
import com.harana.designer.frontend.common.{CaseInsensitiveOrdering, SortOrdering}
import com.harana.designer.frontend.common.SortOrdering._
import com.harana.designer.frontend.common.grid.GridStore._
import com.harana.designer.frontend.common.grid.ui.GridPageItem
import com.harana.designer.frontend.common.ui.{FilterItem, ViewMode}
import com.harana.designer.frontend.user.UserStore.SetPreference
import com.harana.designer.frontend.utils.http.Http
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.{Id, Parameter, ParameterValue}
import diode.AnyAction.aType
import diode._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global

abstract case class GridHandler[Entity <: Id, EditState](entityType: EntityType, state: ModelRW[State, GridState[Entity, EditState]])(implicit decoder: Decoder[Entity], encoder: Encoder[Entity]) extends ActionHandler(state) {

  private val sortOrderingPreferenceId = s"designer.$entityType.sortOrdering"
  private val searchQueryPreferenceId = s"designer.$entityType.filter.searchQuery"
  private val tagPreferenceId = s"designer.$entityType.filter.tag"
  private val viewModePreferenceId = s"designer.$entityType.viewMode"

  def toGridPageItem(entity: Entity): GridPageItem
  def toEntity(editedItem: Option[Entity], subType: Option[EntitySubType], values: Map[ParameterName, ParameterValue]): Entity

  def onInit(userPreferences: Map[String, String]): Option[Effect] = None
  def onNewOrEdit: Option[Effect] = None
  def onNewOrEditChange(parameter: Parameter): Option[Effect] = None
  def onCreate(subType: Option[EntitySubType]): Option[Effect] = None
  def onDelete(subType: Option[EntitySubType]): Option[Effect] = None

  def gridHandle: Option[PartialFunction[Any, ActionResult[State]]] = None
  override def handle = if (gridHandle.isDefined) gridHandle.get.orElse(commonHandle) else commonHandle
  def commonHandle: PartialFunction[Any, ActionResult[State]] = {

    case Init(preferences) =>
      effectOnly(
        Effect.action(Block(entityType)) >>
          (
            Effect.action(
              ActionBatch(
                UpdateSortOrdering(entityType, preferences.get(sortOrderingPreferenceId).map(SortOrdering.withName).getOrElse(SortOrdering.NameAscending)),
                UpdateSearchQuery(entityType, preferences.get(searchQueryPreferenceId)),
                UpdateTag(entityType, preferences.get(tagPreferenceId).flatMap(tag => value.tags.find(_.title == tag))),
                UpdateViewMode(entityType, preferences.get(viewModePreferenceId).map(ViewMode.withName).getOrElse(ViewMode.Grid))
              )
            ) >>
            onInit(preferences).getOrElse(Effect.action(NoAction)) +
            Effect(Http.getRelativeAs[List[Entity]](s"/api/$entityType").map(data =>
              ActionBatch(
                UpdateEntities(entityType, data.getOrElse(List())),
                UpdateItems(entityType, data.getOrElse(List()).map(toGridPageItem)))
              )
            ) +
            Effect.action(RefreshSidebar(entityType))
          ) >>
          Effect.action(Unblock(entityType))
      )


    case NewItem(e, title) =>
      if (e.equals(entityType))
        effectOnly(
          Effect.action(UpdateSelectedItem(e, None)) >>
          onNewOrEdit.getOrElse(Effect.action(NoAction)) >>
          Effect.action(ShowNewOrEditDialog(e, title))
        )
      else
        noChange


    case EditSelectedItem(e, title) =>
      if (e.equals(entityType))
        effectOnly(
          onNewOrEdit.getOrElse(Effect.action(NoAction)) >>
          Effect.action(ShowNewOrEditDialog(e, title))
        )
      else
        noChange


    case ReceiveEvent(_, _, _) =>
      noChange


    case ChangeEditParameter(e, parameter, values) =>
      if (e.equals(entityType) && state.value.editValues != values)
        effectOnly(
          Effect.action(UpdateEditValues(e, values)) >>
          onNewOrEditChange(parameter).getOrElse(Effect.action(NoAction)) >>
          Effect.action(UpdateNewOrEditDialog(e))
        )
      else
        noChange


    case RefreshSidebar(e) =>
      if (e.equals(entityType))
        effectOnly(
          Effect(Http.getRelativeAs[Map[String, Int]](s"/api/$entityType/owners").map(owners => UpdateOwners(entityType, owners.getOrElse(Map())))) +
          Effect(Http.getRelativeAs[Map[String, Int]](s"/api/$entityType/tags").map(tags => UpdateTags(entityType, tags.getOrElse(Map()))))
        )
      else noChange


    case Block(e) =>
      if (e.equals(entityType)) updated(value.copy(blocked = true)) else noChange


    case Unblock(e) =>
      if (e.equals(entityType)) updated(value.copy(blocked = false)) else noChange


    case Filter(e, query, owner, tag) =>
      if (e.equals(entityType)) {
        val url = (query, owner, tag) match {
          case (Some(q), _, Some(t)) => s"/api/$entityType/search/$q?tag=${t.title}"
          case (Some(q), _, None) => s"/api/$entityType/search/$q"
          case (None, _, Some(t)) => s"/api/$entityType?tag=${t.title}"
          case (None, None, None) => s"/api/$entityType"
        }
        effectOnly(
          Effect.action(Block(entityType)) >>
          Effect(Http.getRelativeAs[List[Entity]](url).map(data => UpdateItems(entityType, data.getOrElse(List()).map(toGridPageItem)))) >>
          Effect.action(Unblock(entityType))
        )
      } else noChange


    case SaveNewItem(e, parameterValues) =>
      if (e.equals(entityType)) {
        val entityTypeItem = toEntity(None, value.entitySubType, parameterValues)
        effectOnly(
          Effect.action(Block(e)) >>
          onCreate(value.entitySubType).getOrElse(Effect.action(NoAction)) >>
          Effect(Http.postRelative(s"/api/$entityType", List(), entityTypeItem.asJson.noSpaces).map(_ => NoAction)) >>
          Effect.action(UpdateItems(e, value.items :+ toGridPageItem(entityTypeItem))) >>
          Effect.action(RefreshSidebar(e)) >>
          Effect.action(Unblock(e))
        )
      } else noChange


    case SaveExistingItem(e, entityTypeId, parameterValues) =>
      if (e.equals(entityType))
        effectOnly(
          Effect.action(Block(e)) >>
            Effect(
              for {
                existingItem  <- Http.getRelativeAs[Entity](s"/api/$entityType/$entityTypeId")
                entity        =  toEntity(existingItem, value.entitySubType, parameterValues)
                _             <- Http.putRelative(s"/api/$entityType", List(), entity.asJson.noSpaces)
                action        =  UpdateItems(e, value.items.map(x => if (x.id == entity.id) toGridPageItem(entity) else x))
              } yield action
            ) >>
            Effect.action(RefreshSidebar(e)) >>
            Effect.action(Unblock(e))
        )
      else noChange


    case DeleteSelectedItem(e) =>
      if (e.equals(entityType)) {
        onDelete(value.entitySubType)

        effectOnly(
          Effect.action(Block(e)) >>
            Effect(Http.deleteRelative(s"/api/$entityType/${value.selectedItem.get.id}").map(_ => NoAction)) >>
            Effect.action(UpdateItems(e, value.items.filterNot(_.equals(value.selectedItem.get)))) >>
            Effect.action(RefreshSidebar(e)) >>
            Effect.action(Unblock(e))
        )
      } else noChange


    case Sort(e) =>
      if (e.equals(entityType))
        value.sortOrdering match {
          case NameAscending => updated(value.copy(items = value.items.sortBy(_.title)(CaseInsensitiveOrdering)))
          case NameDescending => updated(value.copy(items = value.items.sortBy(_.title)(CaseInsensitiveOrdering).reverse))
          case CreatedAscending => updated(value.copy(items = value.items.sortBy(_.created)))
          case CreatedDescending => updated(value.copy(items = value.items.sortBy(_.created)(Ordering[Instant].reverse)))
          case UpdatedAscending => updated(value.copy(items = value.items.sortBy(_.updated)))
          case UpdatedDescending => updated(value.copy(items = value.items.sortBy(_.updated)(Ordering[Instant].reverse)))
        }
      else noChange


    case UpdateEditState(e, editState) =>
      if (e.equals(entityType)) updated(value.copy(editState = editState.asInstanceOf[EditState]), Effect.action(UpdateNewOrEditDialog(e))) else noChange


    case UpdateEditParameters(e, parameters) =>
      if (e.equals(entityType)) updated(value.copy(editParameters = parameters), Effect.action(UpdateNewOrEditDialog(e))) else noChange


    case UpdateEditValue(e, k, v) =>
      if (e.equals(entityType)) updated(value.copy(editValues = state.value.editValues + (k -> v))) else noChange


    case UpdateEditValues(e, values) =>
      if (e.equals(entityType)) updated(value.copy(editValues = values)) else noChange


    case UpdateEntities(e, entities) =>
      if (e.equals(entityType)) updated(value.copy(entities = entities.asInstanceOf[List[Entity]])) else noChange


    case UpdateEntitySubType(e, entitySubType) =>
      if (e.equals(entityType)) updated(value.copy(entitySubType = entitySubType)) else noChange


    case UpdateItems(e, items) =>
      if (e.equals(entityType)) updated(value.copy(items = items)) else noChange


    case UpdateOwner(e, owner) =>
      if (e.equals(entityType)) updated(value.copy(owner = owner), Effect.action(Filter(entityType, value.searchQuery, owner, value.tag))) else noChange


    case UpdateOwners(e, owners) =>
      if (e.equals(entityType))
        updated(value.copy(owners = owners.map { case (k, v) => FilterItem(k, v, None) }.toList))
      else
        noChange


    case UpdateSearchQuery(e, searchQuery) =>
      if (e.equals(entityType))
        updated(
          value.copy(searchQuery = searchQuery),
          Effect.action(Filter(entityType, searchQuery, value.owner, value.tag)) + Effect.action(SetPreference(searchQueryPreferenceId, searchQuery))
        )
      else noChange


    case UpdateSelectedItem(e, item) =>
      if (e.equals(entityType))
        updated(value.copy(selectedItem = item, editValues = item.map(_.parameterValues).getOrElse(Map())))
      else noChange


    case UpdateSortOrdering(e, sortOrdering) =>
      if (e.equals(entityType))
        updated(
          value.copy(sortOrdering = sortOrdering),
          Effect.action(Sort(e)) + Effect.action(SetPreference(sortOrderingPreferenceId, Some(sortOrdering.entryName)))
        )
      else noChange


    case UpdateTag(e, tag) =>
      if (e.equals(entityType))
        updated(
          value.copy(tag = tag),
          Effect.action(Filter(entityType, value.searchQuery, value.owner, tag)) + Effect.action(SetPreference(tagPreferenceId, tag.map(_.title)))
        ) else noChange


    case UpdateTags(e, tags) =>
      if (e.equals(entityType))
        updated(value.copy(tags = tags.map { case (k, v) => FilterItem(k, v, None) }.toList))
      else noChange


    case UpdateViewMode(e, viewMode) =>
      if (e.equals(entityType))
        updated(
          value.copy(viewMode = viewMode),
          Effect.action(SetPreference(viewModePreferenceId, Some(viewMode.entryName)))
        )
      else noChange
  }
}