package com.harana.ui.components.cards.horizontal

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class GenericCard extends StatelessComponent {

	case class Props(title: String,
									 subtitle1: String,
									 subtitle1Url: String,
									 subtitle2: String,
									 subtitle2Url: String,
									 description: String,
									 thumbnailUrl: String,
									 linkUrl: String,
									 isNew: Boolean,
									 lastUpdated: String)

	def render() =
		li(className := "media panel panel-body stack-media-on-mobile")(
			div(className := "media-left")(
				a(href := props.linkUrl)(
					img(src := props.thumbnailUrl, className := "img-rounded img-lg")
				)
			),
			div(className := "media-body")(
				h6(className := "media-heading text-semibold")(
					a(href := props.linkUrl)(props.title)
				),
				ul(className := "list-inline list-inline-separate text-muted mb-10")(
					li(
						a(href := props.subtitle1Url)(props.subtitle1)
					),
					li(
						a(href := props.subtitle2Url)(props.subtitle2)
					)
				),
				props.description
			),
			div(className := "media-right text-nowrap")(
				span(className := (if (props.isNew) "label bg-blue" else "text-muted"))(
					if (props.isNew) "New" else props.lastUpdated
				)
			)
		)
}
