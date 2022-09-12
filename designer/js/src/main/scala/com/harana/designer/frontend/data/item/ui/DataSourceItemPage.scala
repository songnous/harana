package com.harana.designer.frontend.data.item.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.data.item.DataSourceItemStore._
import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.designer.frontend.utils.DateUtils
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.ui.components.elements.Page
import com.harana.ui.components.sidebar._
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks.useEffect
import slinky.web.html._

import java.util.concurrent.atomic.AtomicReference
import scala.scalajs.js

@react object DataSourceItemPage {

  val currentDataSource = new AtomicReference[String]("")

  val component = FunctionalComponent[js.Dynamic] { props =>
    val state = Circuit.state(zoomTo(_.dataSourceItemState))

    useEffect(() => {
      val dataSourceId = props.selectDynamic("match").params.selectDynamic("id").toString
      if (!currentDataSource.get.equals(dataSourceId)) {
        Circuit.dispatch(OpenDataSource(dataSourceId))
        currentDataSource.set(dataSourceId)
      }
    })

    Page(
      title = i"heading.section.data",
      subtitle = state.dataSource.map(_.title),
      navigationBar = Some(Navigation(())),
      sidebar = Some(
        Sidebar(
          List(SidebarSection(Some(i"datasources.sidebar.about"), allowCollapse = false, allowClear = false, None, 
            TextListSection(List(
              TextListItem(i"datasources.sidebar.updated", state.dataSource.map(d => Left(d.updated))),
              TextListItem(i"datasources.sidebar.type", state.dataSourceType.map(dst => Right(i"datasources.${dst.syncDirection.value}.${dst.name}.title")))
            ))
          ))
        )
      ),
      content = 
        div(className := "panel panel-flat")(
          div(className := "panel-body panel-fullscreen panel-centered")("Preview not available for this data source")
        )
    )
  }
}