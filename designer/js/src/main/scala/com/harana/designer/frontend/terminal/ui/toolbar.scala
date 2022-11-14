package com.harana.designer.frontend.terminal.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.common.SortOrdering._
import com.harana.designer.frontend.files.FilesStore._
import com.harana.designer.frontend.terminal.TerminalStore.{CopyFromTerminal, PasteToTerminal, ScrollTerminalToBottom, ScrollTerminalToTop}
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.ui.components.LinkType
import com.harana.ui.components.elements.HeadingItem
import com.harana.ui.external.shoelace.{MenuDivider, MenuItem, MenuLabel}

object toolbar {

  val copyPaste = HeadingItem.IconButtonGroup(
    List(
      HeadingItem.IconButton(("icomoon", "copy"), i"common.menu.sort", LinkType.Action(CopyFromTerminal)),
      HeadingItem.IconButton(("icomoon", "paste"), i"common.menu.sort", LinkType.Action(PasteToTerminal)),
    )
  )


  val navigation = HeadingItem.IconButtonGroup(
    List(
      HeadingItem.IconButton(("icomoon", "upload5"), i"common.menu.sort", LinkType.Action(ScrollTerminalToTop)),
      HeadingItem.IconButton(("icomoon", "download5"), i"common.menu.sort", LinkType.Action(ScrollTerminalToBottom)),
    )
  )

  val options = HeadingItem.IconMenu(("icomoon", "menu7"), i"common.menu.sort", className = Some("heading-icon"), menuItems = List(
    MenuLabel(i"common.menu.sort.ascending").withKey("ascending"),
    MenuItem(i"common.menu.sort.ascending.name", iconPrefix = Some("lindua", "sort-alpha-asc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(NameAscending)))).withKey("ascending-name"),
    MenuItem(i"common.menu.sort.ascending.size", iconPrefix = Some("lindua", "sort-numeric-asc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(SizeAscending)))).withKey("ascending-size"),
    MenuItem(i"common.menu.sort.ascending.created", iconPrefix = Some("lindua", "sort-time-asc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(CreatedAscending)))).withKey("ascending-created"),
    MenuItem(i"common.menu.sort.ascending.updated", iconPrefix = Some("lindua", "sort-time-asc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(UpdatedAscending)))).withKey("ascending-updated"),

    MenuDivider().withKey("sort-divider"),

    MenuLabel(i"common.menu.sort.descending").withKey("descending"),
    MenuItem(i"common.menu.sort.descending.name", iconPrefix = Some("lindua", "sort-alpha-desc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(NameDescending)))).withKey("descending-name"),
    MenuItem(i"common.menu.sort.descending.size", iconPrefix = Some("lindua", "sort-numeric-desc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(SizeDescending)))).withKey("descending-size"),
    MenuItem(i"common.menu.sort.descending.created", iconPrefix = Some("lindua", "sort-time-desc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(CreatedDescending)))).withKey("descending-created"),
    MenuItem(i"common.menu.sort.descending.updated", iconPrefix = Some("lindua", "sort-time-desc"), onClick = Some(_ => Circuit.dispatch(UpdateSortOrdering(UpdatedDescending)))).withKey("descending-updated")
  ))

}
