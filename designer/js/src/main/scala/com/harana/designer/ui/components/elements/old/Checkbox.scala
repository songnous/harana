package com.harana.ui.components.elements.old

import com.harana.ui.components.{HorizontalPosition, cssSet}
import com.harana.ui.components.elements.Color
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}

@react class Checkbox extends StatelessComponent {

	case class Props(title: String,
									 checked: Boolean = false,
									 disabled: Boolean = false,
									 inline: Boolean = false,
									 onChange: Option[Boolean => Unit] = None,
									 position: HorizontalPosition = HorizontalPosition.Left,
									 color: Option[Color] = None)

	def render() = {
		val labelAndInput = label(className := cssSet("checkbox-inline" -> props.inline))(
			input(
				`type` := "checkbox",
				checked := props.checked,
				disabled := props.disabled,
				onChange := { e => g.console.dir(e.target)}
			),
			props.title
		)

		if (props.inline) {
			labelAndInput
		}else{
			div(className := cssSet(
				"checkbox" -> true,
				"disabled" -> props.disabled,
				"checkbox-right" -> props.position.value.equals(HorizontalPosition.Right.value)))(
				labelAndInput
			)
		}
	}
}