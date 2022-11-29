package com.harana.designer.frontend.common.grid

import com.harana.designer.frontend.common.SortOrdering
import com.harana.designer.frontend.common.grid.ui.GridPageItem
import com.harana.designer.frontend.common.ui.{FilterItem, ViewMode}
import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup}
import com.harana.sdk.shared.utils.HMap
import diode.{Action => DiodeAction}

object GridStore {
  type EntityType = String
  type EntitySubType = String

  case class GridState[E, S](blocked: Boolean,
                             entities: List[E],
                             items: List[GridPageItem[E]],
                             selectedItem: Option[GridPageItem[E]],
                             entitySubType: Option[EntitySubType],
                             viewMode: ViewMode,
                             searchQuery: Option[String],
                             sortOrdering: SortOrdering,
                             tags: List[FilterItem],
                             tag: Option[FilterItem],
                             owners: List[FilterItem],
                             owner: Option[FilterItem],
                             editParameterGroups: List[ParameterGroup],
                             editValues: HMap[Parameter.Values],
                             additionalState: S)

  def initialState[E, S](additionalState: S) =
    GridState[E, S](false, List(), List(), None, None, ViewMode.Grid, None, SortOrdering.NameAscending, List(), None, List(), None, List.empty[ParameterGroup], HMap.empty, additionalState)

  case class Init(userPreferences: Map[String, String]) extends DiodeAction
  case class ReceiveEvent(entityType: EntityType, eventType: String, eventParameters: Map[String, String]) extends DiodeAction
  case class RefreshSidebar(entityType: EntityType) extends DiodeAction

  case class Block(entityType: EntityType) extends DiodeAction
  case class Unblock(entityType: EntityType) extends DiodeAction

  case class Filter(entityType: EntityType, query: Option[String], owner: Option[FilterItem], tag: Option[FilterItem]) extends DiodeAction
  case class Sort(entityType: EntityType) extends DiodeAction

  case class OnNewItem(entityType: EntityType, dialogTitle: Option[String]) extends DiodeAction
  case class OnEditSelectedItem(entityType: EntityType, dialogTitle: Option[String]) extends DiodeAction
  case class OnEditParameterChange(entityType: EntityType, parameter: Parameter[_], value: Any) extends DiodeAction
  case class OnDeleteItem(entityType: EntityType) extends DiodeAction

  case class SaveNewItem(entityType: EntityType, parameterValues: HMap[Parameter.Values]) extends DiodeAction
  case class SaveExistingItem(entityType: EntityType, entityId: EntityId, parameterValues: HMap[Parameter.Values]) extends DiodeAction

  case class UpdateAdditionalState[S](entityType: EntityType, additionalState: S) extends DiodeAction
  case class UpdateEditParameters(entityType: EntityType, parameters: List[ParameterGroup]) extends DiodeAction
  case class UpdateEditParameterValue(entityType: EntityType, name: Parameter[_], value: Any) extends DiodeAction
  case class UpdateEditParameterValues(entityType: EntityType, values: HMap[Parameter.Values]) extends DiodeAction
  case class UpdateEntitySubType(entityType: EntityType, subType: Option[EntitySubType]) extends DiodeAction
  case class UpdateEntities[E](entityType: EntityType, entities: List[E]) extends DiodeAction
  case class UpdateItems[E](entityType: EntityType, gridItems: List[GridPageItem[E]]) extends DiodeAction
  case class UpdateOwner(entityType: EntityType, owner: Option[FilterItem]) extends DiodeAction
  case class UpdateOwners(entityType: EntityType, owners: Map[String, Int]) extends DiodeAction
  case class UpdateSearchQuery(entityType: EntityType, searchQuery: Option[String]) extends DiodeAction
  case class UpdateSelectedItem[E](entityType: EntityType, item: Option[GridPageItem[E]]) extends DiodeAction
  case class UpdateSortOrdering(entityType: EntityType, ordering: SortOrdering) extends DiodeAction
  case class UpdateTag(entityType: EntityType, tag: Option[FilterItem]) extends DiodeAction
  case class UpdateTags(entityType: EntityType, tags: Map[String, Int]) extends DiodeAction
  case class UpdateViewMode(entityType: EntityType, mode: ViewMode) extends DiodeAction

  case class ShowEditDialog(entityType: EntityType, title: Option[String]) extends DiodeAction
  case class RefreshEditDialog(entityType: EntityType, values: HMap[Parameter.Values]) extends DiodeAction
}