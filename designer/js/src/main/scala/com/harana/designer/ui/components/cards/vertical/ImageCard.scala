package com.harana.ui.components.cards.vertical

import com.harana.ui.components.when
import com.harana.ui.components.Url
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class ImageCard extends StatelessComponent {

	case class Props(link: Url,
									 thumbnail: Url,
									 title: Option[String],
									 description: Option[String])

	def render() =
		div(className := "thumbnail")(
			div(className := "thumb")(
				img(src := props.thumbnail),
				div(className := "caption-overflow")(
					span(
						a(href := props.link, data-"popup" := "lightbox", className := "btn border-white text-white btn-flat btn-icon btn-rounded")(
							i(className := "icon-plus3")
						)
					)
				)
			),
			div(className := "caption")(
				h6(className := "no-margin-top text-semibold")(
					when(props.title, p(className := "text-default")(props.title.get)),
					when(props.description,
						p(className := "text-muted")(
							i(className := "icon-download pull-right"),
							props.description.get
						)
					)
				)
			)
		)
}
