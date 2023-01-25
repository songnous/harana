package com.harana.ui.components.elements.old

import com.harana.ui.components.elements.Color
import com.harana.ui.components.{HorizontalPosition, _}
import enumeratum.values.{StringEnum, StringEnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html.{i, _}

@react class FloatingActionButton extends StatelessComponent {

	case class Props(buttonOpenIcon: Option[String] = None,
                   buttonCloseIcon: Option[String] = None,
                   buttonColor: Option[Color] = None,
                   menuItems: List[FloatingButtonMenuItem] = List(),
                   menuOpenBehaviour: Option[MenuOpenBehaviour] = None,
                   menuOpenDirection: Option[MenuOpenBehaviour] = None,
                   menuPosition: Option[MenuPosition] = None,
                   menuPositionType: Option[MenuPositionType] = None,
                   displayMenuOnPageLoad: Boolean = false)

	def menuItem(menuItem: FloatingButtonMenuItem) =
		li(
			div(className := cssSet(
				"fab-left-right" -> optEnum(menuItem.labelPosition).equals(HorizontalPosition.Right.value),
				"fab-label-visible" -> menuItem.labelAlwaysVisible.getOrElse(false)
			), data-"fab-label" := menuItem.label)(
				a(href := menuItem.url, className := cssSet(
					"btn btn-default btn-rounded btn-icon btn-float" -> true,
					"dropdown-toggle" -> menuItem.menu.nonEmpty,
					s"bg-${menuItem.color.get.value}" -> menuItem.color.nonEmpty
				), data-"toggle" := (if (menuItem.menu.nonEmpty) "dropdown" else ""))(
					when(menuItem.icon, i(className := menuItem.icon.map { i => s"icon-$i" })),
					when(menuItem.image, img(src := menuItem.image))
				),
				when(menuItem.badge),
				when(menuItem.statusMark, span(className := "status-mark bg-pink-400")),
				when(menuItem.menu)
			)
		)

	def render() =
		ul(className := cssSet(
			"fab-menu" -> true,
			s"fab-menu-${optEnum(props.menuOpenDirection)}" -> props.menuOpenDirection.nonEmpty,
			s"fab-menu-${optEnum(props.menuPosition)}" -> props.menuPosition.nonEmpty,
			s"fab-menu-${optEnum(props.menuPositionType)}" -> props.menuPositionType.nonEmpty
		), data-"fab-toggle" := optEnum(props.menuOpenBehaviour))(
			li(
				a(className := cssSet(
					"fab-menu-btn btn btn-float btn-rounded btn-icon" -> true,
					s"bg-${props.buttonColor.map(_.value).orNull}" -> props.buttonColor.nonEmpty,

				))(
					i(className := props.buttonOpenIcon.getOrElse("icon-plus3")),
					i(className := props.buttonCloseIcon.getOrElse("icon-cross2")),
				),
				ul(className := "fab-menu-inner")(
					props.menuItems.map(menuItem)
				)
			)
		)
}

case class FloatingButtonMenuItem(label: String,
																	url: String,
																	icon: Option[String] = None,
																	image: Option[String] = None,
																	badge: Option[Badge] = None,
																	color: Option[Color] = None,
																	labelPosition: Option[HorizontalPosition] = None,
																	labelAlwaysVisible: Option[Boolean] = None,
																	menu: Option[Menu] = None,
																	menuPosition: Option[HorizontalPosition] = None,
																	statusMark: Boolean = false)

sealed abstract class MenuOpenBehaviour(val value: String) extends StringEnumEntry
case object MenuOpenBehaviour extends StringEnum[MenuOpenBehaviour] {
	case object Click extends MenuOpenBehaviour("click")
	case object Hover extends MenuOpenBehaviour("hover")
	val values = findValues
}

sealed abstract class MenuOpenDirection(val value: String) extends StringEnumEntry
case object MenuOpenDirection extends StringEnum[MenuOpenBehaviour] {
	case object Top extends MenuOpenDirection("top")
	case object Bottom extends MenuOpenDirection("bottom")
	val values = findValues
}

sealed abstract class MenuPosition(val value: String) extends StringEnumEntry
case object MenuPosition extends StringEnum[MenuPosition] {
	case object TopLeft extends MenuPosition("top-left")
	case object TopRight extends MenuPosition("top-right")
	case object BottomLeft extends MenuPosition("bottom-left")
	case object BottomRight extends MenuPosition("bottom-right")
	val values = findValues
}

sealed abstract class MenuPositionType(val value: String) extends StringEnumEntry
case object MenuPositionType extends StringEnum[MenuPositionType] {
	case object Absolute extends MenuPositionType("absolute")
	case object Fixed extends MenuPositionType("fixed")
	case object Affixed extends MenuPositionType("affixed")
	val values = findValues
}