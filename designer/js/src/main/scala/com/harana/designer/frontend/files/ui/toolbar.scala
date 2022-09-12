package com.harana.designer.frontend.files.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.common.SortOrdering.{CreatedAscending, CreatedDescending, NameAscending, NameDescending, SizeAscending, SizeDescending, UpdatedAscending, UpdatedDescending}
import com.harana.designer.frontend.files.FilesStore._
import com.harana.designer.frontend.utils.FileUtils
import com.harana.ui.components.elements.{Dialog, HeadingItem}
import com.harana.ui.external.shoelace.{MenuDivider, MenuItem, MenuLabel}
import com.harana.designer.frontend.utils.i18nUtils.ops
import slinky.core.facade.ReactRef

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object toolbar {

  val sort = HeadingItem.IconMenu(("icomoon", "sort-alpha-asc"), i"common.menu.sort", className = Some("heading-icon"), menuItems = List(
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

  def create(ref: ReactRef[Dialog.Def], state: FilesState) = HeadingItem.IconMenu(("icomoon", "plus3"), i"files.menu.new", className = Some("heading-icon"), menuItems = List(
    MenuItem(
      label = i"files.menu.new.new-folder",
      iconPrefix = Some("lindua", "folder-plus"),
      onClick = Some(_ => dialogs.newFolder(ref))
    ).withKey("new-folder"),

    MenuItem(
      label = i"files.menu.new.upload-files",
      iconPrefix = Some("lindua", "upload"),
      onClick = Some(_ => dialogs.uploadFiles(ref, state))
    ).withKey("upload-files")
  ))

  def pathTree(state: FilesState) =
    HeadingItem.IconMenu(("icomoon", "arrow-up8"), i"files.menu.new", className = Some("heading-icon"), menuItems =
      List(
        MenuItem(
          label = i"files.menu.pathtree.home",
          iconPrefix = Some("icomoon", "home6"),
          onClick = Some(_ => Circuit.dispatch(PopToHome))
        ).withKey("pathtree-home")
      ) ++ (
        if (state.item.isDefined)
          if (state.item.get.path.isEmpty)
            List()
          else
            state.item.get.path.split("/").filter(_.nonEmpty).dropRight(1).zipWithIndex.map { case (folder, index) =>
              MenuItem(
                label = folder,
                iconPrefix = Some("icomoon", "folder4"),
                onClick = Some(_ => Circuit.dispatch(PopPath(index+1)))
              ).withKey(s"pathtree-$folder")
            }
        else
          List()
        )
    )

  def edit(ref: ReactRef[Dialog.Def], state: FilesState) = HeadingItem.IconMenu(("icomoon", "menu7"), i"files.menu.edit", className = Some("heading-icon"), enabled = state.selectedFile.isDefined, menuItems =
    if (state.selectedFile.isEmpty)
      List()
    else
      List(
        MenuItem(
          label = i"files.menu.edit.open",
          iconPrefix = Some("lindua", "file-open"),
          onClick = Some(_ => Circuit.dispatch(PushPath(state.selectedFile.get.name)))
        ),

        MenuDivider(),

        MenuItem(
          label = i"files.menu.edit.edit-info",
          iconPrefix = Some("lindua", "file-pencil"),
          onClick = Some(_ => dialogs.editInfo(ref, state)),
          disabled = Some(state.selectedFile.get.isFolder)
        ),

        MenuItem(
          label = i"files.menu.edit.duplicate",
          iconPrefix = Some("lindua", "files"),
          onClick = Some(_ => Circuit.dispatch(DuplicateItem)),
          disabled = Some(false)
        ),

        MenuItem(
          label = i"files.menu.edit.delete",
          iconPrefix = Some("lindua", "trash"),
          onClick = Some(_ => dialogs.deleteFiles(ref))
        ),

        MenuDivider(),

        MenuItem(
          label = i"files.menu.edit.download",
          iconPrefix = Some("lindua", "download"),
          onClick = Some(_ => Circuit.dispatch(DownloadItem)),
          disabled = Some(state.selectedFile.get.isFolder)
        ),

        MenuDivider(),

        MenuItem(
          label = i"files.menu.edit.compress",
          iconPrefix = Some("lindua", "file-compressed"),
          onClick = Some(_ => Circuit.dispatch(CompressItem)),
          disabled = Some(FileUtils.isArchive(state.selectedFile.get))
        ),
        MenuItem(
          label = i"files.menu.edit.decompress",
          iconPrefix = Some("lindua", "file-compressed"),
          onClick = Some(_ => Circuit.dispatch(DecompressItem)),
          disabled = Some(!FileUtils.isArchive(state.selectedFile.get))
        )
      ))
}
