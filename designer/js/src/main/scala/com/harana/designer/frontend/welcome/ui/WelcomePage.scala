package com.harana.designer.frontend.welcome.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.designer.frontend.welcome.WelcomeStore.State
import com.harana.ui.components.elements.Page
import com.harana.ui.external.shoelace.Icon
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html._

import scala.scalajs.js

@react object WelcomePage {

  val component = FunctionalComponent[js.Dynamic] { props =>
    val state = Circuit.state(zoomTo(_.welcomeState))
    val title = "Welcome"

    Page(
      title = title,
      navigationBar = Some(Navigation(())),
      content = pageContent(state)
    )
  }


  def pageContent(state: State) = {
    div(className := "flow-container")(
      div(className := "panel panel-flat")(
        div(className := "table-responsive")(
          Icon(library = Some("icomoon"), name = "stack4", className = Some("welcome-icon"))
        )
      )
    )
  }
}