package com.harana.designer.frontend.flows.list

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.Main
import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.common.grid.GridHandler
import com.harana.designer.frontend.common.grid.GridStore.{EntitySubType, UpdateEditParameters}
import com.harana.designer.frontend.common.grid.ui.GridPageItem
import com.harana.designer.frontend.flows.list.FlowListStore._
import com.harana.designer.frontend.utils.ColorUtils
import com.harana.sdk.shared.models.common.{Background, Visibility}
import com.harana.sdk.shared.models.flow.Flow
import com.harana.sdk.shared.models.flow.graph.FlowGraph
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, StringArrayParameter, StringParameter}
import com.harana.sdk.shared.utils.HMap
import com.harana.ui.components.LinkType
import diode.Effect

import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

class FlowListHandler extends GridHandler[Flow, FlowEditState]("flows", zoomTo(_.flowListState)) {

  def toGridPageItem(flow: Flow) =
    GridPageItem(
      id = null,
      title = flow.title,
      description = Some(flow.description),
      tags = flow.tags,
      created = flow.created,
      updated = flow.updated,
      chartType = None,
      link = LinkType.Page(s"/flows/${flow.id}"),
      entitySubType = None,
      background = Some(flow.background),
      parameterValues = HMap[Parameter.Values](
        (GridPageItem.titleParameter, flow.title),
        (GridPageItem.descriptionParameter, flow.description),
        (GridPageItem.tagsParameter, flow.tags)
      ),
    )

  def toEntity(editedItem: Option[Flow], subType: Option[EntitySubType], values: HMap[Parameter.Values]) =
    editedItem
      .getOrElse(
        Flow(
          title = "",
          description = "",
          connections = List(),
          graph = FlowGraph(),
          createdBy = Some(Main.claims.userId),
          visibility = Visibility.Owner,
          background = Background.Image(ColorUtils.randomBackground),
          tags = Set()
        )
      )
      .copy(
        title = values.getOrElse(GridPageItem.titleParameter, ""),
        description = values.getOrElse(GridPageItem.descriptionParameter, ""),
        tags = values.getOrElse(GridPageItem.tagsParameter, Set.empty[String]),
      )

  override def onInit(preferences: Map[String, String]) =
    Some(
      Effect.action(UpdateEditParameters("flows", List(ParameterGroup("about",
        StringParameter("title", required = true),
        StringParameter("description", multiLine = true, required = true),
        StringArrayParameter("tags")
    )))))

  override def onCreate(subType: Option[EntitySubType]) = {
    Analytics.flowCreate()
    None
  }

  override def onDelete(subType: Option[EntitySubType]) = {
    Analytics.flowDelete()
    None
  }
}