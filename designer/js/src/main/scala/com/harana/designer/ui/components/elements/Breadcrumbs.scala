package com.harana.ui.components.elements

import com.harana.ui.components._
import com.harana.ui.components.Url
import com.harana.ui.components.elements.old.{BadgeStyle, Menu}
import enumeratum.values.{StringEnum, StringEnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class Breadcrumbs extends StatelessComponent {

	case class Props(items: List[BreadcrumbItem],
									 rightElements: List[ReactElement] = List(),
									 isComponent: Boolean = false,
									 backgroundColor: Option[Color] = None,
									 arrowStyle: Option[BreadcrumbsArrowStyle] = None)

	def render() =
		div(className := cssSet(
			"breadcrumb-line" -> true,
			"breadcrumb-line-component content-group-lg" -> props.isComponent,
			s"bg-${props.backgroundColor.map(_.value).orNull}" -> props.backgroundColor.nonEmpty
		))(
			ul(className := cssSet(
				"breadcrumb" -> true,
				s"breadcrumb-${props.arrowStyle.map(_.value).orNull}" -> props.arrowStyle.nonEmpty
			))(
				props.items.map { item =>
					li(className := (if (item.isActive) "active" else ""))(
						a(href := item.link.getOrElse("#"))(
							i(className := item.icon),
							item.text
						),
						when(item.menu)
					)
				}
			),
			ul(className := "breadcrumb-elements")(
				props.rightElements.map(li(_))
			)
		)
}

case class BreadcrumbItem(text: String,
													link: Option[Url] = None,
													icon: Option[String] = None,
													isActive: Boolean = false,
													menu: Option[Menu] = None)

sealed abstract class BreadcrumbsArrowStyle(val value: String) extends StringEnumEntry
case object BreadcrumbsArrowStyle extends StringEnum[BadgeStyle] {
	case object Arrow extends BreadcrumbsArrowStyle("arrow")
	case object Arrows extends BreadcrumbsArrowStyle("arrows")
	case object Caret extends BreadcrumbsArrowStyle("caret")
	case object Dash extends BreadcrumbsArrowStyle("dash")

	val values = findValues
}