package com.harana.ui.components.elements

import com.harana.ui.components.sidebar.Sidebar
import com.harana.ui.components._
import com.harana.ui.external.helmet.Helmet
import com.harana.ui.external.shoelace.{Button, ButtonGroup, Dropdown, Menu}
import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, ReactElement}
import slinky.web.html._

@react class Page extends StatelessComponent {

	case class Props(title: String,
									 subtitle: Option[String] = None,
									 subtitleMenu: Option[Menu.Props] = None,
									 navigationBar: Option[ReactElement] = None,
									 fixedNavigationBar: Boolean = true,
									 footerNavigationBar: Option[ReactElement] = None,
									 toolbarItems: List[HeadingItem] = List(),
									 blocked: Boolean = false,
									 leftSidebar: Option[Ref[Sidebar]] = None,
									 rightSidebar: Option[Ref[Sidebar]] = None,
									 content: ReactElement,
									 noScrollingContent: Boolean = false,
									 fullSizedContent: Boolean = false,
									 fixedSizeContent: Boolean = false)

	def render() = {
		Fragment(
			Helmet(
				title(s"harana: ${props.title}")
			),
			div(className := cssSet(
				"page-header page-header-inverse" -> true,
				"page-header-fixed" -> props.fixedNavigationBar,
				"no-margin" -> props.fullSizedContent))(
				when(props.navigationBar, props.navigationBar.get),
				when(!props.fullSizedContent)(
					div(className := "page-header-content")(
						div(className := "page-title")(headingTitle),
						when(props.toolbarItems.nonEmpty)(
							div(className := "heading-elements")(
								Toolbar(props.toolbarItems)
							)
						)
					)
				)
			),
			div(className := (if (props.blocked) "block-ui-cursor" else ""))(
				div(className := cssSet(
					"page-container" -> !props.fixedSizeContent,
					"no-scrolling-content" -> props.noScrollingContent,
					"fixed-size-content" -> props.fixedSizeContent,
					"full-size-content" -> props.fullSizedContent,
					"block-ui" -> props.blocked,
					"non-block-ui" -> !props.blocked)
				)(
						whenRef(props.leftSidebar),
						(props.leftSidebar, props.rightSidebar) match {
							case (None, None) 			=> div(className := "content")(props.content)
							case (None, Some(_)) 		=> div(className := "content")(props.content)
							case (Some(_), None) 		=> div(className := "content-wrapper")(props.content)
							case (Some(_), Some(_)) => div(className := "content-wrapper content-wrapper-right")(props.content)
						},
						whenRef(props.rightSidebar),
				)
			),
			when(props.footerNavigationBar, props.footerNavigationBar.get)
		)
	}

	def headingTitle =
		Fragment(
			h4(className := "left-header-element")(props.title),
			when(props.subtitle,
				Fragment(
					p(className := "left-header-element")("/"),
					props.subtitleMenu match {
						case Some(menu) =>
							Dropdown(
								button = Some(Button.Props(
									caret = Some(true),
									content = Some(h6(props.subtitle.get)),
									variant = Some("text")
								)),
								className = Some("left-header-element"),
								menu = Some(menu)
							)
						case None =>
							h6(props.subtitle.get)
					}
				)
			)
		)
}