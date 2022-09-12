package com.harana.designer.frontend.flows.list

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.common.grid.GridHandler
import com.harana.designer.frontend.common.grid.GridStore.{EntitySubType, UpdateEditParameters}
import com.harana.designer.frontend.common.grid.ui.GridPageItem
import com.harana.designer.frontend.{Circuit, Main}
import com.harana.sdk.shared.models.common.{Background, Parameter, ParameterGroup, ParameterValue, Visibility}
import com.harana.ui.components.LinkType
import com.harana.designer.frontend.utils.ColorUtils
import com.harana.designer.frontend.flows.list.FlowListStore._
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import diode.Effect

import scala.concurrent.ExecutionContext.Implicits.global

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
      background = flow.background,
      parameterValues = Map(
        "title" -> ParameterValue.String(flow.title),
        "description" -> ParameterValue.String(flow.description),
        "tags" -> ParameterValue.StringList(flow.tags.toList)
      )
  )

  def toEntity(editedItem: Option[Flow], subType: Option[EntitySubType], values: Map[ParameterName, ParameterValue]) =
    editedItem
      .getOrElse(
        Flow(
          title = "",
          description = "",
          connections = List(),
          actions = List(),
          links = List(),
          createdBy = Some(Main.claims.userId),
          visibility = Visibility.Owner,
          background = Some(Background.Image(ColorUtils.randomBackground)),
          tags = Set()
        )
      )
      .copy(
        title = values("title").asInstanceOf[ParameterValue.String],
        description = values("description").asInstanceOf[ParameterValue.String],
        tags = values.get("tags").map(_.asInstanceOf[ParameterValue.StringList]).map(_.toSet).getOrElse(Set())
      )

  override def onInit(preferences: Map[String, String]) =
    Some(
      Effect.action(UpdateEditParameters("flows", List(
      ParameterGroup("about", List(
        Parameter.String("title", required = true),
        Parameter.String("description", multiLine = true, required = true),
        Parameter.StringList("tags"),
      ))
    ))))

  override def onCreate(subType: Option[EntitySubType]) = {
    Analytics.flowCreate()
    None
  }

  override def onDelete(subType: Option[EntitySubType]) = {
    Analytics.flowDelete()
    None
  }
}