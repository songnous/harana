package com.harana.designer.frontend.files.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.files.FilesStore._
import com.harana.designer.frontend.files.ui.FilesPage.dialogRef
import com.harana.designer.frontend.user.UserStore.{SaveSettings, UpdateSettings, UserState}
import com.harana.sdk.shared.utils.Random
import com.harana.ui.components.elements.Dialog
import com.harana.ui.components.sidebar._
import com.harana.ui.external.shoelace.IconButton
import diode.ActionBatch
import slinky.core.facade.ReactRef
import slinky.web.html.{className, div}

object sidebar {

  def home(filesState: FilesState, userState: UserState, ref: ReactRef[Dialog.Def]) = Sidebar(List(
    SidebarSection(title = Some("Search"), content = SearchSection(onSearch = (search: Option[String]) => ())),
    SidebarSection(title = Some("Sharing"), content = SwitchSection(List(
      Switch(
        label = "File Sharing",
        checked = userState.settings.exists(_.fileSharingEnabled),
        onClick = checked =>
          if (checked)
            Circuit.dispatch(
              ActionBatch(
                UpdateSettings(userState.settings.map(_.copy(fileSharingEnabled = true, fileSharingUsername = Some(Random.long), fileSharingPassword = Some(Random.long)))),
                SaveSettings,
                RefreshSharingContent
              )
            )
          else
            Circuit.dispatch(
              ActionBatch(
                UpdateSettings(userState.settings.map(_.copy(fileSharingEnabled = false, fileSharingUsername = None, fileSharingPassword = None))),
                SaveSettings,
                RefreshSharingContent
              )
            ),
        rightElement = Some(IconButton(library = Some("icomoon"), name = "info", className = Some("switch-icon"), onClick = Some(_ => dialogs.mountDrive(dialogRef, filesState))))
      ),
      Switch(
        label = "Remote Login",
        checked = userState.settings.exists(_.remoteLoginEnabled),
        onClick = checked =>
          if (checked)
            Circuit.dispatch(
              ActionBatch(
                UpdateSettings(userState.settings.map(_.copy(remoteLoginEnabled = true, remoteLoginUsername = Some(Random.long), remoteLoginPassword = Some(Random.long)))),
                SaveSettings,
                RefreshSharingContent
              )
            )
          else
            Circuit.dispatch(
              ActionBatch(
                UpdateSettings(userState.settings.map(_.copy(remoteLoginEnabled = false, remoteLoginUsername = None, remoteLoginPassword = None))),
                SaveSettings,
                RefreshSharingContent
              )
            ),
        rightElement = Some(
          div(className := "switch-div")
          (IconButton(library = Some("icomoon"), name = "info", className = Some("switch-icon"), onClick = Some(_ => dialogs.connectViaSFTP(dialogRef))))
        )
      )
    )))
  ))
}