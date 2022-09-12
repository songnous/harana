package com.harana.designer.frontend.schedules.item.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.data.item.DataSourceItemStore._
import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.designer.frontend.schedules.item.ScheduleItemStore.OpenSchedule
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.ui.components.elements.Page
import com.harana.ui.components.sidebar._
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks.useEffect
import slinky.web.html._

import java.util.concurrent.atomic.AtomicReference
import scala.scalajs.js

@react object ScheduleItemPage {

  val currentSchedule = new AtomicReference[String]("")

  val component = FunctionalComponent[js.Dynamic] { props =>
    val state = Circuit.state(zoomTo(_.scheduleItemState))

    useEffect(() => {
      val scheduleId = props.selectDynamic("match").params.selectDynamic("id").toString
      if (!currentSchedule.get.equals(scheduleId)) {
        Circuit.dispatch(OpenSchedule(scheduleId))
        currentSchedule.set(scheduleId)
      }
    })

    Page(
      title = i"heading.section.schedules",
      subtitle = state.schedule.map(_.title),
      navigationBar = Some(Navigation(())),
      sidebar = Some(
        Sidebar(
          List(SidebarSection(Some(i"schedules.sidebar.about"), allowCollapse = false, allowClear = false, None,
            TextListSection(List(
              TextListItem(i"schedules.sidebar.updated", state.schedule.map(d => Left(d.updated)))
            ))
          ))
        )
      ),
      content = 
        div(className := "panel panel-flat")(
          div(className := "panel-body panel-fullscreen panel-centered")("Preview not available for this schedule")
        )
    )
  }
}