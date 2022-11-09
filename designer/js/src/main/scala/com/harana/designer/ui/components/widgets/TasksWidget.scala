package com.harana.ui.components.widgets

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class TasksWidget extends StatelessComponent {

	case class Props()

	def render() =
		div(className := "panel panel-flat")(
			div(className := "panel-heading")(
				h6(className := "panel-title")("My Tasks"),
				div(className := "heading-elements")(
					span(className := "label bg-indigo-400 heading-text")("43 new")
				)
			),
			div(className := "panel-body")(
				ul(className := "media-list content-group")(
					li(className := "media")(
						div(className := "media-left")(
							input(`type` := "checkbox", className := "styled", id := "task3")
						),
						div(className := "media-body")(
							h6(className := "media-heading")(
								label(htmlFor := "task3", className := "no-margin text-semibold cursor-pointer")("The him father parish")
							),
							"Reran sincere said monkey one slapped jeepers"
						)
					),
					li(className := "media")(
						div(className := "media-body text-center")(
							a(href := "#", className := "text-muted")("View completed tasks (16)")
						)
					)
				),
				textarea(name := "enter-message", className := "form-control content-group", rows := "1", cols := "1", placeholder := "New task..."),

				div(className := "row")(
					div(className := "col-xs-6")(
						ul(className := "icons-list icons-list-extended mt-10")(
							li(a(href := "#")(i(className := "icon-mic2"))),
							li(a(href := "#")(i(className := "icon-file-picture"))),
							li(a(href := "#")(i(className := "icon-file-plus")))
						)
					),
					div(className := "col-xs-6 text-right")(
						button(`type` := "button", className :="btn bg-pink-400")("Create task")
					)
				)
			)
		)
}