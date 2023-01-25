package com.harana.ui.components.elements

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.{Circuit, Globals}
import com.harana.designer.frontend.system.SystemStore
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.ui.components.{LinkType, Url, _}
import com.harana.ui.external.shoelace._
import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, ReactElement}
import slinky.core.{CustomAttribute, FunctionalComponent}
import slinky.web.html._

@react object NavigationBar {

	case class Props(leftItems: List[(NavigationBarItem, Option[LinkType])],
									 rightItems: List[(NavigationBarItem, Option[LinkType])],
									 activeItem: Option[NavigationBarItem],
									 position: Option[NavigationBarPosition],
									 size: Option[NavigationBarSize],
									 style: List[NavigationBarStyle] = List(),
									 logoImageUrl: Url,
									 logoLink: LinkType,
									 onChange: Option[NavigationBarItem => Unit] = None)

	val component = FunctionalComponent[Props] { props =>
		div(className := cssSet(
			"navbar" -> true,
			s"navbar-${optEnum(props.position)}" -> props.position.nonEmpty,
			s"navbar-${optEnum(props.size)}" -> props.size.nonEmpty,
			s"${props.style.map { style => s"navbar-${style.value}" }.mkString(" ")}" -> true
		))(
			div(className := "navbar-header")(
				Link(props.logoLink, Some("navbar-brand"))(List(img(key := "logo", src := props.logoImageUrl)))
			),
			div(className := "navbar-collapse collapse")(
				items(props, props.leftItems, isRight = false),
				items(props, props.rightItems, isRight = true)
			)
		)
	}

	private def items(props: Props, items: List[(NavigationBarItem, Option[LinkType])], isRight: Boolean) =
		ul(className := cssSet(
			"nav navbar-nav" -> true,
			"navbar-right" -> isRight
		))(
			items.zipWithIndex.map { case (item, index) =>
				li(key := index.toString, className := cssSet(
					"active" -> (props.activeItem.nonEmpty && item._1.equals(props.activeItem.get)),
					"dropdown-user" -> item._1.userTitle.nonEmpty
				)) {
					val cls = item._1.item.flatMap {
						case (ItemType.Button(_), _) => Some("navbar-btn-link")
						case (_, _) => None
					}

					if (item._2.nonEmpty)
						Link(item._2.get, cls)(account(item._1))
					else
						account(item._1)
				}
			}
		)

	private def account(item: NavigationBarItem): List[ReactElement] =
		List(
			item.userTitle match {
				case Some(title) =>
					Fragment(
						Dropdown(
							element = Some(
								div(CustomAttribute[String]("slot") := "trigger")(
									if (item.userImage.nonEmpty) img(src := item.userImage.get) else Avatar(className = Some("avatar"), initials = item.userTitle.map(_.head.toString)),
									span(title),
									Button(className = Some("navigationbar-caret"), label = Some(""), caret = Some(true), `type` = Some("text"))
								)
							),
							menu = Some(Menu.Props(items = List(
								MenuLabel(i"heading.account"),
								a(href := s"https://${Globals.authDomain}/billing/portal")(MenuItem(i"heading.account.billing-portal")),
								MenuDivider(),
								MenuLabel(i"heading.support"),
								a(href := "https://github.com/harana/feedback/issues/new?&labels=bug&template=bug.md", target := "_blank")(MenuItem(i"heading.support.bug-report", static = true)),
								Link(LinkType.Action(SystemStore.ToggleDebug))(
									List(
										MenuItem(i"heading.support.enable-debugging", iconPrefix = if (Circuit.state(zoomTo(_.systemState)).debug) Some("icomoon", "checkmark3") else None))
								),
								a(href := "https://github.com/harana/feedback/issues/new?&labels=feature&template=feature.md", target := "_blank")(MenuItem(i"heading.support.feature-request", static = true)),
								a(href := "mailto:support@harana.com", target := "_blank")(MenuItem(i"heading.support.contact-us", static = true))
							))))
					)
				case None =>
					Item(item.title, item.item).withKey(item.title.getOrElse(""))
			}
		)
}

case class NavigationBarItem(title: Option[String],
														 item: Option[(ItemType, ItemPosition)] = None,
														 showCaret: Boolean = false,
														 userImage: Option[String] = None,
														 userTitle: Option[String] = None)

sealed abstract class NavigationBarPosition(val value: String) extends StringEnumEntry
case object NavigationBarPosition extends StringEnum[NavigationBarPosition] {
	case object FixedTop extends NavigationBarPosition("fixed-top")
	case object FixedBottom extends NavigationBarPosition("fixed-bottom")
	case object StaticTop extends NavigationBarPosition("static-top")
	val values = findValues
}

sealed abstract class NavigationBarSize(val value: String) extends StringEnumEntry
case object NavigationBarSize extends StringEnum[NavigationBarSize] with StringCirceEnum[NavigationBarSize] {
	case object Large extends NavigationBarSize("lg")
	case object Small extends NavigationBarSize("sm")
	case object Mini extends NavigationBarSize("xs")
	val values = findValues
}

sealed abstract class NavigationBarStyle(val value: String) extends StringEnumEntry
case object NavigationBarStyle extends StringEnum[NavigationBarStyle] {
	case object Transparent extends NavigationBarStyle("transparent")
	case object Inverse extends NavigationBarStyle("inverse")
	case class Colored(color: Color) extends NavigationBarStyle(s"bg-${color.value}")
	val values = findValues
}