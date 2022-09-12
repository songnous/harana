package com.harana.designer.frontend.navigation.ui

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.navigation.NavigationStore.OpenCheckout
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.designer.frontend.{Circuit, Main}
import com.harana.ui.components.LinkType
import com.harana.ui.components.elements._
import com.harana.ui.external.shoelace.Button
import org.scalajs.dom.window
import slinky.core.FunctionalComponent
import slinky.core.annotations.react


@react object Navigation {

  val alertsItem = NavigationBarItem(Some(i"heading.section.alerts"), Some(ItemType.Icon("icomoon", "stack4"), ItemPosition.Left))
  val appsItem = NavigationBarItem(Some(i"heading.section.apps"), Some(ItemType.Icon("icomoon", "stack4"), ItemPosition.Left))
  val catalogsItem = NavigationBarItem(Some(i"heading.section.catalogs"), Some(ItemType.Icon("icomoon", "archive"), ItemPosition.Left))
  val containersItem = NavigationBarItem(Some(i"heading.section.containers"), Some(ItemType.Icon("icomoon", "cube2"), ItemPosition.Left))
  val dataItem = NavigationBarItem(Some(i"heading.section.data"), Some(ItemType.Icon("icomoon", "database"), ItemPosition.Left))
  val filesItem = NavigationBarItem(Some(i"heading.section.files"), Some(ItemType.Icon("icomoon", "file-text2"), ItemPosition.Left))
  val flowsItem = NavigationBarItem(Some(i"heading.section.flows"), Some(ItemType.Icon("icomoon", "design"), ItemPosition.Left))
  val modelsItem = NavigationBarItem(Some(i"heading.section.models"), Some(ItemType.Icon("icomoon", "cube2"), ItemPosition.Left))
  val projectsItem = NavigationBarItem(Some(i"heading.section.projects"), Some(ItemType.Icon("icomoon", "folder"), ItemPosition.Left))
  val queriesItem = NavigationBarItem(Some(i"heading.section.queries"), Some(ItemType.Icon("icomoon", "glyphSearch"), ItemPosition.Left))
  val referralItem = NavigationBarItem(None, Some(ItemType.Icon("icomoon", "trophy4"), ItemPosition.Left))
  val schedulesItem = NavigationBarItem(Some(i"heading.section.schedules"), Some(ItemType.Icon("icomoon", "calendar3"), ItemPosition.Left))
  val servicesItem = NavigationBarItem(Some(i"heading.section.services"), Some(ItemType.Icon("icomoon", "upload10"), ItemPosition.Left))
  val snippetsItem = NavigationBarItem(Some(i"heading.section.snippets"), Some(ItemType.Icon("icomoon", "scissors"), ItemPosition.Left))
  val storiesItem = NavigationBarItem(Some(i"heading.section.stories"), Some(ItemType.Icon("icomoon", "magazine"), ItemPosition.Left))
  val storeItem = NavigationBarItem(Some(i"heading.section.store"), Some(ItemType.Icon("icomoon", "bag"), ItemPosition.Left))
  val terminalItem = NavigationBarItem(Some(i"heading.section.terminal"), Some(ItemType.Icon("icomoon", "bag"), ItemPosition.Left))


  val component = FunctionalComponent[Unit] { _ =>
    val state = Circuit.state(zoomTo(_.navigationState))

    val billingCheckoutItem = NavigationBarItem(
      title = None,
      item = Some((ItemType.Button(Button.Props(
        label = Some(i"heading.signup"),
        loading = Some(state.openingCheckout),
        `type` = Some("primary"),
        className = Some("signup-button"),
        onClick = Some(_ => Circuit.dispatch(OpenCheckout))
      )), ItemPosition.Right))
    )

    val userProfileItem = NavigationBarItem(None, None, showCaret = true, Main.claims.imageUrl, Some(Main.claims.firstName))

    val navigationItems = List(
      filesItem -> Some(LinkType.Page("/files")),
      dataItem -> Some(LinkType.Page("/data")),
      flowsItem -> Some(LinkType.Page("/flows")),
      appsItem -> Some(LinkType.Page("/apps")),
      alertsItem -> Some(LinkType.Page("/schedules")),
      terminalItem -> Some(LinkType.Page("/schedules")))

    NavigationBar(
      leftItems = navigationItems,
      rightItems = if (Main.claims.subscriptionEnded.isEmpty) List(userProfileItem -> None) else List(billingCheckoutItem -> None, userProfileItem -> None),
      activeItem = navigationItems.find(item => item._2.isDefined && s"${window.location.pathname}".startsWith(item._2.get.name)).map(_._1),
      position = None,
      size = None,
      style = List(NavigationBarStyle.Inverse, NavigationBarStyle.Transparent),
      logoImageUrl = "/public/images/logo.png",
      logoLink = LinkType.Page("/")
    )
  }
}