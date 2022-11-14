package com.harana.designer.frontend.help.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.help.HelpStore.State
import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.ui.components.elements.Page
import com.harana.ui.components.sidebar.{NavigationGroup, NavigationItem, NavigationSection, Sidebar, SidebarSection}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html._

import scala.scalajs.js

@react object HelpPage {

  val component = FunctionalComponent[js.Dynamic] { props =>
    val state = Circuit.state(zoomTo(_.helpState))
    val title = "Help Guide"

    Page(
      title = title,
      navigationBar = Some(Navigation(())),
      content = pageContent(state),
      sidebar = Some(sidebar(state))
    )
  }


  def sidebar(state: State) = {
    val categories = state.categories.map { category =>
      val pages = category.pages.map { page =>
        NavigationItem(title = page.name, onClick = () => ())
      }
      NavigationGroup(pages, Some(category.name))
    }
    Sidebar(List(SidebarSection(Some("Categories"), content = NavigationSection(categories))))
  }


  def pageContent(state: State) = {
    div(className := "flow-container")(
      div(className := "panel panel-flat")(
        div(className := "table-responsive")(
        )
      )
    )
  }
}
