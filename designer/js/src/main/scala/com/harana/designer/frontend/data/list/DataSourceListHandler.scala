package com.harana.designer.frontend.data.list

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.common.grid.GridHandler
import com.harana.designer.frontend.common.grid.GridStore.{EntitySubType, UpdateEditParameters, UpdateEditState, UpdateEditValue}
import com.harana.designer.frontend.common.grid.ui.GridPageItem
import com.harana.designer.frontend.data.list.DataSourceListStore.DataSourceEditState
import com.harana.designer.frontend.utils.ColorUtils
import com.harana.designer.frontend.utils.http.Http
import com.harana.designer.frontend.Main
import com.harana.sdk.shared.models
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common._
import com.harana.sdk.shared.models.data.{DataSource, DataSourceType, SyncDirection}
import com.harana.ui.components.LinkType
import com.harana.ui.components.widgets.PillChartType
import diode.AnyAction.aType
import diode._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DataSourceListHandler extends GridHandler[DataSource, DataSourceEditState]("datasources", zoomTo(_.dataSourceListState)) {

  def toGridPageItem(dataSource: DataSource) =
    GridPageItem(
      id = dataSource.id,
      title = dataSource.title,
      description = Some(dataSource.description),
      tags = dataSource.tags,
      created = dataSource.created,
      updated = dataSource.updated,
      chartType = Some(PillChartType.Bar),
      link = LinkType.Page(s"/data/${dataSource.id}"),
      entitySubType = None,
      background = dataSource.background,
      additionalData = Map("type" -> dataSource.dataSourceType),
      parameterValues = Map(
        "title" -> ParameterValue.String(dataSource.title),
        "description" -> ParameterValue.String(dataSource.description),
        "tags" -> ParameterValue.StringList(dataSource.tags.toList)
      ) ++ dataSource.parameterValues
    )


  def toEntity(editedItem: Option[DataSource], subType: Option[EntitySubType], values: Map[ParameterName, ParameterValue]) =
    editedItem
      .getOrElse(
        models.data.DataSource(
          title = "",
          description= "",
          dataSourceType = null,
          path = None,
          createdBy = Some(Main.claims.userId),
          visibility = Visibility.Owner,
          background = Some(Background.Image(ColorUtils.randomBackground)),
          tags = Set(),
          parameterValues = Map()
        )
      ).copy(
        title = values.get("title").map(_.asInstanceOf[ParameterValue.String].value).getOrElse(""),
        description = values.get("description").map(_.asInstanceOf[ParameterValue.String].value).getOrElse(""),
        tags = values.get("tags").map(_.asInstanceOf[ParameterValue.StringList]).map(_.toSet).getOrElse(Set()),
        dataSourceType = values.get("type").map(_.asInstanceOf[ParameterValue.String].value).getOrElse(""),
        parameterValues = values
      )


  def aboutGroup(dataSourceTypes: List[String]) = ParameterGroup("about", List(
    Parameter.String("title", required = true),
    Parameter.String("description", multiLine = true, required = true),
    Parameter.StringList("tags"),
    Parameter.String("direction",
      default = Some(ParameterValue.String(SyncDirection.Source.value)),
      options = List(
        ("source", ParameterValue.String("source")),
        ("destination", ParameterValue.String("destination"))
      ),
      required = true
    ),
    Parameter.String("type",
      options = dataSourceTypes.map(s => (s, ParameterValue.String(s))),
      required = true
    ),
  ))


  private def fetchDataSourceTypes(direction: String) =
    Http.getRelativeAs[List[String]](s"/api/datasources/types/$direction").map(_.getOrElse(List()))


  private def updateDataSourceTypes(direction: SyncDirection) =
    if (state.value.editState.dataSourceTypes.contains(direction.value))
      Effect.action(NoAction)
    else
      Effect(fetchDataSourceTypes(direction.value.toLowerCase).map { dsTypes =>
        UpdateEditState("datasources", state.value.editState.copy(dataSourceTypes = state.value.editState.dataSourceTypes + (direction.value.toLowerCase -> dsTypes)))
      })


  private def fetchDataSourceType(direction: String, dsType: String) =
    Http.getRelativeAs[DataSourceType](s"/api/datasources/type/$dsType")


  private def direction =
    state.value.editValues.get("direction").map(_.asInstanceOf[ParameterValue.String].value).getOrElse(SyncDirection.Source.value)


  private def dataSourceType =
    state.value.editValues.get("type").map(_.asInstanceOf[ParameterValue.String].value).getOrElse(state.value.editState.dataSourceTypes(direction).head)


  private def onDataSourceTypeChanged(direction: String, dsType: String) = {
    val dsTypes = state.value.editState.dataSourceTypes

    (dsType, dsTypes) match {
      case (dsType, dsTypes) =>
        fetchDataSourceType(direction, dsType).map(ds =>
          ActionBatch(
            UpdateEditParameters("datasources", List(aboutGroup(dsTypes(direction))) ++ ds.get.parameterGroups),
            UpdateEditValue("datasources", "type", ParameterValue.String(dsType))
          )
        )
      case _ =>
        Future(NoAction)
    }
  }


  override def onInit(userPreferences: Map[String, String]) =
    Some(updateDataSourceTypes(SyncDirection.Source) + updateDataSourceTypes(SyncDirection.Destination))


  override def onNewOrEdit =
    Some(
      onInit(Map()).get >>
      Effect.action(UpdateEditValue("datasources", "direction", ParameterValue.String(direction))) >>
      Effect(onDataSourceTypeChanged(direction, dataSourceType))
    )


  override def onNewOrEditChange(parameter: Parameter) =
    Some(
      Effect(
        parameter.name match {
          case "direction" => onDataSourceTypeChanged(direction, state.value.editState.dataSourceTypes(direction).head)
          case "type" => onDataSourceTypeChanged(direction, dataSourceType)
          case _ => Future(NoAction)
        }
      )
    )


  override def onCreate(subType: Option[EntitySubType]) = {
    Analytics.dataSourceCreate()
    None
  }


  override def onDelete(subType: Option[EntitySubType]) = {
    Analytics.dataSourceDelete()
    None
  }
}