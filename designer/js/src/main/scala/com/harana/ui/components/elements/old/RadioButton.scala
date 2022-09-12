package com.harana.ui.components.elements.old

import com.harana.ui.components.{HorizontalPosition, cssSet}
import com.harana.ui.components.elements.Color
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class RadioButton extends StatelessComponent {

	case class Props(title: String,
									 checked: Boolean = false,
									 disabled: Boolean = false,
									 inline: Boolean = false,
									 position: HorizontalPosition = HorizontalPosition.Left,
									 color: Option[Color] = None)

	def render() = {
		val labelAndInput = label(className := cssSet("radio-inline" -> props.inline))(
			input(
				`type` := "radio",
				className := "styled",
				checked := props.checked,
				disabled := props.disabled),
			props.title
		)

		if (props.inline) {
			labelAndInput
		}else{
			div(className := cssSet(
				"radio" -> true,
				"disabled" -> props.disabled,
				"radio-right" -> props.position.value.equals(HorizontalPosition.Right.value)))(
				labelAndInput
			)
		}
	}
}