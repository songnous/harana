package com.harana.ui.components.elements.old

import com.harana.ui.components.elements.Color
import com.harana.ui.components.{Size, _}
import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

import scala.scalajs.js

@react class ProgressBar extends StatelessComponent {

	case class Props(percentage: Int,
									 showPercentage: Boolean = true,
									 style: Option[ProgressBarStyle] = None,
									 size: Option[Size] = None,
									 color: Option[Color] = None)

	def render() =
		div(
			className := cssSet(
				"progress" -> true,
				"progress-rounded" -> (optEnum(props.style) == ProgressBarStyle.Rounded.value),
				props.style.getOrElse(ProgressBarStyle.Default).value -> props.style.isDefined
			)
		)(
			div(
				className := cssSet(
					"progress-bar" -> true,
					("progress-" + props.size.getOrElse(Size.Mini).value) -> props.size.isDefined
				),
				style := js.Dynamic.literal(width = s"${props.percentage}%")
			)(span(
					className := cssSet("sr-only" -> props.showPercentage)
				)(s"${props.percentage}%")
			)
		)
}

sealed abstract class ProgressBarStyle(val value: String) extends StringEnumEntry
case object ProgressBarStyle extends StringEnum[ProgressBarStyle] with StringCirceEnum[ProgressBarStyle] {
	case object Default extends ProgressBarStyle("default")
	case object Striped extends ProgressBarStyle("progress-striped")
	case object Animated extends ProgressBarStyle("progress-striped.active")
	case object Rounded extends ProgressBarStyle("progress-rounded")
	val values = findValues
}