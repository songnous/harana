package com.harana.designer.frontend.data.list

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.Main
import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.common.grid.GridHandler
import com.harana.designer.frontend.common.grid.GridStore.{EntitySubType, UpdateEditParameterValue, UpdateEditParameters, UpdateAdditionalState}
import com.harana.designer.frontend.common.grid.ui.GridPageItem
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

class DataSourceListHandler extends GridHandler[DataSource, DataSourceListStore.State]("datasources", zoomTo(_.dataSourceListState)) {

  val directionParameter = StringParameter("direction",
    default = Some(SyncDirection.Source.value),
    options = List(
      ("source", "source"),
      ("destination", "destination")
    ),
    required = true)

  var typeParameter = StringParameter("type", options = List())

  def toGridPageItem(dataSource: DataSource) = {
    GridPageItem(
      id = dataSource.id,
      title = dataSource.title,
      description = Some(dataSource.description),
      tags = dataSource.tags,
      created = dataSource.created,
      updated = dataSource.updated,
      entity = dataSource,
      chartType = Some(PillChartType.Bar),
      link = Some(LinkType.Page(s"/data/${dataSource.id}")),
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


  private def fetchDataSourceTypes(direction: String) =
    Http.getRelativeAs[List[String]](s"/api/datasources/types/direction/$direction").map(_.getOrElse(List()))


  private def fetchDataSourceType(dsType: String) =
    Http.getRelativeAs[DataSourceType](s"/api/datasources/types/$dsType")


  // 1. Update the type dropdown with new data sources
  // 2. Add the additional data source parameters
  private def updateDataSourceType(direction: String, dsType: String) = {
    val dsTypes = state.value.additionalState.dataSourceTypes
    typeParameter = StringParameter("type", options = dsTypes(direction).map(s => (s, s)))

    Effect.action(UpdateEditParameterValue("datasources", typeParameter, dsType)) >>
    Effect(fetchDataSourceType(dsType).map(ds => UpdateEditParameters("datasources",
      List(ParameterGroup("about",
        GridPageItem.titleParameter,
        GridPageItem.descriptionParameter,
        GridPageItem.tagsParameter,
        directionParameter,
        typeParameter
      )) ++ ds.get.parameterGroups))
    )
  }

  // Cache the data source types in both directions
  override def onInit(userPreferences: Map[String, String]) =
    Some(
      Effect(
        for {
          sourceTypes         <- fetchDataSourceTypes("source")
          destinationTypes    <- fetchDataSourceTypes("destination")
        } yield
          UpdateAdditionalState("datasources", state.value.additionalState.copy(
            dataSourceTypes = Map(
             "source"       -> sourceTypes,
             "destination"  -> destinationTypes
            )
          ))
      )
    )


  override def onEdit =
    Some(
      updateDataSourceType(SyncDirection.Source.value, state.value.additionalState.dataSourceTypes(SyncDirection.Source.value).head)
    )


  override def onEditParameterWillChange(parameter: Parameter[_], value: Any) = {
    parameter.name match {
      case "direction"  =>
        val direction = value.asInstanceOf[String]
        Some(updateDataSourceType(direction, state.value.additionalState.dataSourceTypes(direction).head))

      case "type"       =>
        val direction = state.value.editValues.getOrElse(directionParameter, SyncDirection.Source.value)
        Some(updateDataSourceType(direction, value.asInstanceOf[String]))

      case _            => None
    }
  }


  override def onSave(subType: Option[EntitySubType]) = {
    Analytics.dataSourceCreate()
    None
  }


  override def onDelete(subType: Option[EntitySubType]) = {
    Analytics.dataSourceDelete()
    None
  }
}