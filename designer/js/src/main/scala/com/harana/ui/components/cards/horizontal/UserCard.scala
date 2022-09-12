package com.harana.ui.components.cards.horizontal

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class UserCard extends StatelessComponent {

	case class Props()

	def render() =
		div(className := "panel panel-body")(
			div(className := "media")(
				div(className := "media-left")(
					a(href :=" assets/images/demo/images/3.png", data-"popup" := "lightbox")(
						img(src := "../../../../global_assets/images/placeholders/placeholder.jpg", className := "img-circle img-lg", alt := "")
					)
				),

				div(className := "media-body")(
					h6(className := "media-heading")("James Alexander"),
					span(className := "text-muted")("Lead developer")
				),

				div(className := "media-right media-middle")(
					ul(className := "icons-list")(
						li(className := "dropdown")(
							a(href := "#", className := "dropdown-toggle", data-"toggle" := "dropdown")(
								i(className := "icon-menu7")
							),
							ul(className := "dropdown-menu dropdown-menu-right")(
								li(a(href := "#")(i(className := "icon-comment-discussion pull-right"), "Start chat")),
								li(a(href := "#")(i(className := "icon-phone2 pull-right"), "Make a call")),
								li(a(href := "#")(i(className := "icon-mail5 pull-right"), "Send mail")),
								li(className := "divider"),
								li(a(href := "#")(i(className := "icon-statistics pull-right"), "Statistics"))
							)
						)
					)
				)
			)
		)
}
