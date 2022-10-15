package com.harana.designer.frontend.data.list

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.Main
import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.common.grid.GridHandler
import com.harana.designer.frontend.common.grid.GridStore.{EntitySubType, UpdateEditParameters, UpdateEditState, UpdateEditParameterValue}
import com.harana.designer.frontend.common.grid.ui.GridPageItem
import com.harana.designer.frontend.data.list.DataSourceListStore.DataSourceEditState
import com.harana.designer.frontend.utils.ColorUtils
import com.harana.designer.frontend.utils.http.Http
import com.harana.sdk.shared.models.common.{Background, Visibility}
import com.harana.sdk.shared.models.data.{DataSource, DataSourceType, SyncDirection}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, StringParameter}
import com.harana.sdk.shared.utils.HMap
import com.harana.ui.components.LinkType
import com.harana.ui.components.widgets.PillChartType
import diode.AnyAction.aType
import diode._

import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

class DataSourceListHandler extends GridHandler[DataSource, DataSourceEditState]("datasources", zoomTo(_.dataSourceListState)) {

  val directionParameter = StringParameter("direction",
    default = Some(SyncDirection.Source.value),
    options = List(
      ("source", "source"),
      ("destination", "destination")
    ),
    required = true)

  var typeParameter: StringParameter = _

  def toGridPageItem(dataSource: DataSource) = {
    GridPageItem(
      id = dataSource.id,
      title = dataSource.title,
      description = Some(dataSource.description),
      tags = dataSource.tags,
      created = dataSource.created,
      updated = dataSource.updated,
      chartType = Some(PillChartType.Bar),
      link = LinkType.Page(s"/data/${dataSource.id}"),
      background = dataSource.background,
      additionalData = Map("type" -> dataSource.dataSourceType),
      parameterValues = HMap[Parameter.Values](
        (GridPageItem.titleParameter, dataSource.title),
        (GridPageItem.descriptionParameter, dataSource.description),
        (GridPageItem.tagsParameter, dataSource.tags)
      ) ++ dataSource.parameterValues
    )
  }


  def toEntity(editedItem: Option[DataSource], subType: Option[EntitySubType], values: HMap[Parameter.Values]) =
    editedItem
      .getOrElse(
        DataSource(
          title = "",
          description= "",
          dataSourceType = null,
          path = None,
          createdBy = Some(Main.claims.userId),
          visibility = Visibility.Owner,
          background = Some(Background.Image(ColorUtils.randomBackground)),
          tags = Set(),
          parameterValues = HMap.empty[Parameter.Values]
        )
      ).copy(
        title = values.getOrElse(GridPageItem.titleParameter, ""),
        description = values.getOrElse(GridPageItem.descriptionParameter, ""),
        tags = values.getOrElse(GridPageItem.tagsParameter, Set.empty[String]),
        dataSourceType = values.getOrElse(typeParameter, ""),
        parameterValues = values
      )


  def aboutGroup =
    ParameterGroup("about",
      GridPageItem.titleParameter,
      GridPageItem.descriptionParameter,
      GridPageItem.tagsParameter,
      directionParameter,
      typeParameter
    )


  private def fetchDataSourceTypes(direction: String) =
    Http.getRelativeAs[List[String]](s"/api/datasources/types/direction/$direction").map(_.getOrElse(List()))


  private def updateDataSourceTypes(direction: SyncDirection) =
    if (state.value.editState.dataSourceTypes.contains(direction.value))
      Effect.action(NoAction)
    else
      Effect(fetchDataSourceTypes(direction.value.toLowerCase).map { dsTypes =>
        UpdateEditState("datasources", state.value.editState.copy(dataSourceTypes = state.value.editState.dataSourceTypes + (direction.value.toLowerCase -> dsTypes)))
      })


  private def fetchDataSourceType(dsType: String) =
    Http.getRelativeAs[DataSourceType](s"/api/datasources/types/$dsType")


  private def direction =
    state.value.editValues.getOrElse(directionParameter, SyncDirection.Source.value)


  private def dataSourceType =
    state.value.editValues.getOrElse(typeParameter, state.value.editState.dataSourceTypes(direction).head)


  private def updateAllDataSourceTypes() =
    updateDataSourceTypes(SyncDirection.Source) + updateDataSourceTypes(SyncDirection.Destination)


  private def onDataSourceTypeChanged(direction: String, dsType: String) = {
    val dsTypes = state.value.editState.dataSourceTypes
    (dsType, dsTypes) match {
      case (dsType, dsTypes) =>
        typeParameter = StringParameter("type", options = dsTypes(direction).map(s => (s, s)))
        Effect(fetchDataSourceType(dsType).map(ds => UpdateEditParameters("datasources", List(aboutGroup) ++ ds.get.parameterGroups))) +
        Effect.action(UpdateEditParameterValue("datasources", typeParameter, dsType))
      case _ =>
        Effect.action(NoAction)
    }
  }


  override def onInit(userPreferences: Map[String, String]) =
    Some(
      Effect.action(UpdateEditParameters("datasources", List(aboutGroup))) >> updateAllDataSourceTypes()
    )


  override def onNewOrEdit =
    Some(
      Effect.action(UpdateEditParameterValue("datasources", directionParameter, direction)) >>
      onDataSourceTypeChanged(direction, dataSourceType)
    )


  override def onNewOrEditParameterChange(parameter: Parameter[_]) =
    Some(
        parameter.name match {
          case "direction"  => onDataSourceTypeChanged(direction, state.value.editState.dataSourceTypes(direction).head)
          case "type"       => onDataSourceTypeChanged(direction, dataSourceType)
          case _            => Effect.action(NoAction)
        }
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