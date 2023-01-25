package com.harana.designer.frontend.common.grid.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.common.SortOrdering
import com.harana.designer.frontend.common.SortOrdering.{CreatedAscending, CreatedDescending, NameAscending, NameDescending, UpdatedAscending, UpdatedDescending}
import com.harana.designer.frontend.common.grid.GridStore.{OnDeleteItem, OnEditSelectedItem, EntitySubType, OnNewItem, UpdateEntitySubType, UpdateSelectedItem, UpdateSortOrdering, UpdateViewMode}
import com.harana.designer.frontend.common.ui.ViewMode
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.ui.components.{LinkType, openLink}
import com.harana.ui.components.elements.HeadingItem.IconButton
import com.harana.ui.components.elements.{Dialog, DialogStyle, HeadingItem}
import com.harana.ui.external.shoelace.{MenuDivider, MenuItem, MenuLabel}
import diode.ActionBatch
import slinky.core.facade.{ReactElement, ReactRef}

import scala.collection.mutable.ListBuffer

object menus {

  def headingMenu(viewMode: ViewMode,
                  itemType: String,
                  item: Option[GridPageItem[_]],
                  allowEdit: Boolean,
                  allowDelete: Boolean,
                  deleteDialogRef: ReactRef[Dialog.Def],
                  itemMenuItems: Option[GridPageItem[_] => List[ReactElement]]) =
   HeadingItem.IconMenu(
     icon = ("icomoon", "menu7"),
     tooltip = i"files.menu.edit",
     className = Some("heading-icon"),
     enabled = item.nonEmpty,
     menuItems = item.map(itemMenu(viewMode, itemType, _, allowEdit, allowDelete, deleteDialogRef, itemMenuItems)).getOrElse(List())
   )

  def itemMenu(viewMode: ViewMode,
               itemType: String,
               item: GridPageItem[_],
               allowEdit: Boolean,
               allowDelete: Boolean,
               deleteDialogRef: ReactRef[Dialog.Def],
               itemMenuItems: Option[GridPageItem[_] => List[ReactElement]]) = {
    val items = ListBuffer[ReactElement]()
    if (viewMode == ViewMode.List) {
      items += menus.openItem(item)
      if (allowEdit || allowDelete || itemMenuItems.nonEmpty) items += MenuDivider()
    }
    if (allowEdit) items += menus.editItem(itemType, item)
    if (allowDelete) items += menus.deleteItem(itemType, item, deleteDialogRef)
    if (itemMenuItems.nonEmpty) items += itemMenuItems.get(item)
    items.toList
  }


  def newItem(itemType: String, itemSubTypes: List[(String, EntitySubType)], allowEdit: Boolean) =
    if (allowEdit) List(
      if (itemSubTypes.isEmpty)
        HeadingItem.IconButton(("icomoon", "plus3"), i"common.grid.menu.new", LinkType.OnClick(() =>
          Circuit.dispatch(OnNewItem(itemType, Some(i"common.grid.menu.new")))
        ))
      else
        HeadingItem.IconMenu(("icomoon", "plus3"), i"common.grid.menu.new",
          itemSubTypes.map { se =>
            MenuItem(
              label = se._1,
              iconPrefix = None,
              onClick = Some(_ => {
                Circuit.dispatch(UpdateEntitySubType(itemType, Some(se._2)))
                Circuit.dispatch(OnNewItem(itemType, Some(i"common.grid.menu.new")))
              }),
              value = Some(se._2)
            )
          }, Some("heading-icon"))
    ) else List.empty[HeadingItem]


  def openItem(item: GridPageItem[_]) =
    MenuItem(
      label = i"common.grid.item.menu.open",
      iconPrefix = Some("lindua", "file-pencil"),
      onClick = item.link.map(l => _ => openLink(l))
    )


  def editItem(itemType: String, item: GridPageItem[_]) =
    MenuItem(
      label = i"common.grid.item.menu.edit",
      iconPrefix = Some("lindua", "file-pencil"),
      onClick = Some(_ =>
        Circuit.dispatch(
          ActionBatch(
            UpdateSelectedItem(itemType, Some(item)),
            OnEditSelectedItem(itemType, Some(i"common.grid.item.menu.edit"))
          )
        )
      )
    )

  def deleteItem(itemType: String, item: GridPageItem[_], dialogRef: ReactRef[Dialog.Def]) =
    MenuItem(
      label = i"common.grid.item.menu.delete",
      iconPrefix = Some("lindua", "trash"),
      onClick = Some(_ => dialogRef.current.show(
        title = Some(i"common.grid.item.menu.delete"),
        style = DialogStyle.Confirm(
          confirmLabel = s"Are you sure you want to delete '${item.title}' ?",
          confirmButtonLabel = "Delete",
          onOk = Some(() => Circuit.dispatch(
            ActionBatch(
              UpdateSelectedItem(itemType, Some(item)),
              OnDeleteItem(itemType))
            )
          )
        )
      ))
    )

  def sort(itemType: String, currentSortOrdering: SortOrdering) =
    HeadingItem.IconMenu(
      icon = ("icomoon", currentSortOrdering match {
        case NameAscending => "sort-alpha-asc"
        case CreatedAscending => "sort-time-asc"
        case UpdatedAscending => "sort-time-asc"
        case NameDescending => "sort-alpha-desc"
        case CreatedDescending => "sort-time-desc"
        case UpdatedDescending => "sort-time-desc"
      }),
      tooltip = i"common.menu.sort",
      className = Some("heading-icon"),
      menuItems = List(
        MenuLabel(i"common.menu.sort.ascending"),
        MenuItem(i"common.menu.sort.ascending.name", iconPrefix = Some("lindua", "sort-alpha-asc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(itemType, NameAscending)))),
        MenuItem(i"common.menu.sort.ascending.created", iconPrefix = Some("lindua", "sort-time-asc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(itemType, CreatedAscending)))),
        MenuItem(i"common.menu.sort.ascending.updated", iconPrefix = Some("lindua", "sort-time-asc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(itemType, UpdatedAscending)))),
        MenuDivider(),

        MenuLabel(i"common.menu.sort.descending"),
        MenuItem(i"common.menu.sort.descending.name", iconPrefix = Some("lindua", "sort-alpha-desc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(itemType, NameDescending)))),
        MenuItem(i"common.menu.sort.descending.created", iconPrefix = Some("lindua", "sort-time-desc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(itemType, CreatedDescending)))),
        MenuItem(i"common.menu.sort.descending.updated", iconPrefix = Some("lindua", "sort-time-desc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(itemType, UpdatedDescending))))
      )
    )

  def viewMode(itemType: String, currentViewMode: ViewMode) =
    HeadingItem.IconButtonGroup(
      List(
        IconButton(
          icon = ("icomoon", "list"),
          className = Some("heading-icon"),
          tooltip = i"common.menu.sort",
          enabled = currentViewMode == ViewMode.Grid,
          link = LinkType.Action(UpdateViewMode(itemType, ViewMode.List))
        ),
        IconButton(
          icon = ("icomoon", "grid"),
          className = Some("heading-icon"),
          tooltip = i"common.menu.sort",
          enabled = currentViewMode == ViewMode.List,
          link = LinkType.Action(UpdateViewMode(itemType, ViewMode.Grid))
        )
      )
    )
}