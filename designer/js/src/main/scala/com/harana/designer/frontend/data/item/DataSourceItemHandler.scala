package com.harana.designer.frontend.data.item

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.data.item.DataSourceItemStore._
import com.harana.designer.frontend.utils.http.Http
import com.harana.designer.frontend.State
import com.harana.sdk.shared.models.data.{DataSource, DataSourceType}
import diode._

import scala.concurrent.ExecutionContext.Implicits.global

class DataSourceItemHandler extends ActionHandler(zoomTo(_.dataSourceItemState)) {
  override def handle: PartialFunction[Any, ActionResult[State]] = {

    case Init(preferences) =>
      noChange


    case OpenDataSource(id) =>
      effectOnly(
        Effect(Http.getRelativeAs[DataSource](s"/api/datasources/$id").map(ds => UpdateDataSource(ds))) >>
        Effect(Http.getRelativeAs[DataSourceType](s"/api/datasources/type/$id").map(dst => UpdateDataSourceType(dst)))
      )


    case UpdateDataSource(dataSource) =>
      updated(value.copy(dataSource = dataSource))


    case UpdateDataSourceType(dataSourceType) =>
      updated(value.copy(dataSourceType = dataSourceType))

  }
}