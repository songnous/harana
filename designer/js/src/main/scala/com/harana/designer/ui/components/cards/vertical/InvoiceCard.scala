package com.harana.ui.components.cards.vertical

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class InvoiceCard extends StatelessComponent {

	case class Props(invoice: Invoice,
									 showTitle: Boolean,
									 showSocial: Boolean,
									 showMessaging: Boolean,
									 value: String)

	def render() =
		div(
			div(className := "panel invoice-grid")(
				div(className := "panel-body")(
					div(className := "row")(
						div(className := "col-sm-6")(
							h6(className := "text-semibold no-margin-top")("Leonardo Fellini"),
							ul(className := "list list-unstyled")(
								li("Invoice #: 0028"),
								li("Issued on: ", span(className := "text-semibold")("2015/01/25"))
							)
						),
						div(className := "col-sm-6")(
							h6(className := "text-semibold text-right no-margin-top")("$8,750"),
							ul(className := "list list-unstyled text-right")(
								li("Method: ", span(className := "text-semibold")("SWIFT")),
								li(className := "dropdown")(
									"Status: ",
									a(href :="#", className := "label bg-danger-400 dropdown-toggle", data-"toggle" := "dropdown")("Overdue", span(className := "caret")),
									ul(className := "dropdown-menu dropdown-menu-right")(
										li(className := "active")(a(href := "#")(i(className := "icon-alert"), "Overdue"),
										li(a(href := "#")(i(className := "icon-alarm"),"Pending")),
										li(a(href := "#")(i(className := "icon-checkmark3"),"Paid")),
										li(className := "divider"),
										li(a(href := "#")(i(className := "icon-spinner2 spinner"),"On hold")),
										li(a(href := "#")(i(className := "icon-cross2"),"Canceled"))
									)
								)
							)
						)
					)
				),
				div(className := "panel-footer panel-footer-condensed")(
					div(className := "heading-elements")(
						span(className := "heading-text")(
							span(className := "status-mark border-danger position-left")("Due: ", span(className := "text-semibold")("2015/02/25"))
						),
						ul(className := "list-inline list-inline-condensed heading-text pull-right")(
							li(a(href := "#", className := "text-default", data-"toggle" := "modal", data-"target" := "#invoice")(i(className := "icon-eye8"))),
							li(className := "dropdown")(
								a(href :="#", className := "text-default dropdown-toggle", data-"toggle" := "dropdown")(
									i(className := "icon-menu7"),span(className := "caret")
								),
								ul(className := "dropdown-menu dropdown-menu-right")(
									li(a(href := "#")(i(className := "icon-printer"), "Print invoice")),
									li(a(href := "#")(i(className := "icon-file-download"), "Download invoice")),
									li(className := "divider"),
									li(a(href := "#")(i(className := "icon-file-plus"), "Edit invoice")),
									li(a(href := "#")(i(className := "icon-cross2"), "Remove invoice"))
								)
							)
						)
					)
				)
			)
		)
	)
}

case class Invoice()
