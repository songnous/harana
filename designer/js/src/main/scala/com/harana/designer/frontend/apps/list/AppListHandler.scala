package com.harana.designer.frontend.apps.list

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.apps.list.AppListStore.AppListEditState
import com.harana.designer.frontend.common.grid.GridHandler
import com.harana.designer.frontend.common.grid.GridStore.EntitySubType
import com.harana.designer.frontend.common.grid.ui.GridPageItem
import com.harana.sdk.shared.models.apps.App
import com.harana.sdk.shared.models.common.Visibility
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.utils.HMap
import com.harana.ui.components.LinkType

class AppListHandler extends GridHandler[App, AppListEditState]("apps", zoomTo(_.appListState)) {

  def toGridPageItem(app: App) =
    GridPageItem(
      id = app.id,
      title = app.title,
      description = Some(app.description),
      tags = app.tags,
      created = app.created,
      updated = app.updated,
      chartType = None,
      link = LinkType.Page(s"/apps/${app.id}"),
      entitySubType = None,
      background = app.background,
      parameterValues = HMap[Parameter.Values](
        (GridPageItem.titleParameter, app.title),
        (GridPageItem.descriptionParameter, app.description),
        (GridPageItem.tagsParameter, app.tags)
      )
    )

  def toEntity(editedItem: Option[App], subType: Option[EntitySubType], values: HMap[Parameter.Values]) =
    App("", "", "", "", 0, None, None, Visibility.Owner, None, Set())

}