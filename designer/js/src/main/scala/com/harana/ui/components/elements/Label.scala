package com.harana.ui.components.elements

import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.ui.components.cssSet
import com.harana.ui.components.Url
import enumeratum.values.{StringEnum, StringEnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class Label extends StatelessComponent {

	case class Props(title: String,
									 icon: Option[Item] = None,
									 link: Option[Url] = None,
									 style: Option[LabelStyle] = None,
									 color: Option[Color] = None)

	def render() = {
		val cls = cssSet(
			"label" -> true,
			"bg-" + props.color.map(_.value).orNull -> props.color.isDefined,
			s"label-" + props.style.map(_.value).orNull -> props.style.isDefined
		)

		props.link match {
			case Some(url) => a(href := url, className := cls)(props.title)
			case None => span(className := cls)(props.title)
		}
	}
}

sealed abstract class LabelStyle(val value: String) extends StringEnumEntry
case object LabelStyle extends StringEnum[LabelStyle] {
	case object Rounded extends LabelStyle("rounded")
	case object Roundless extends LabelStyle("roundless")
	case object Block extends LabelStyle("block")
	case object Striped extends LabelStyle("striped")
	case object Flat extends LabelStyle("flat")
	val values = findValues
}