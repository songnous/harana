package com.harana.ui.components.cards.vertical

import com.harana.ui.components.Orientation
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class UserCard extends StatelessComponent {

	case class Props(orientation: Orientation = Orientation.Horizontal,
									 roundedThumbnail: Boolean = true,
									 showSocial: Boolean = true,
									 showMessaging: Boolean = true)

	def render() =
		div(className := "panel")(
			div(className := "panel-body text-center")(
				a(href := "#", className := "display-inline-block")(
					img(src := "../../../../global_assets/images/placeholders/placeholder.png", className := "img-circle img-responsive", alt := "")
				),
				h6(className := "text-semibold no-margin-bottom")(
					a(href := "#", className := "text-default")("Nathan Jacobson"),
					small(className := "display-block")("Lead UX designer")
				)
			),

			div(className := "panel-footer text-center no-padding")(
				div(className := "row")(
					div(className := "col-xs-3")(
						a(href := "#", className := "display-block p-10 text-default", data-"popup" := "tooltip", data-"placement" := "top", data-"container" := "body", title := "Google Drive")(
							i(className := "icon-google-drive")
						)
					),

					div(className := "col-xs-3")(
						a(href := "#", className := "display-block p-10 text-default", data-"popup" := "tooltip", data-"placement" := "top", data-"container" := "body", title := "Twitter")(
							i(className := "icon-twitter")
						)
					),

					div(className := "col-xs-3")(
						a(href := "#", className := "display-block p-10 text-default", data-"popup" := "tooltip", data-"placement" := "top", data-"container" := "body", title := "Github")(
							i(className := "icon-github")
						)
					),

					div(className := "col-xs-3")(
						a(href := "#", className := "display-block p-10 text-default", data-"popup" := "tooltip", data-"placement" := "top", data-"container" := "body", title := "Dribbble")(
							i(className := "icon-dribbble")
						)
					)
				)
			)
		)
}
