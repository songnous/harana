package com.harana.ui.components.sidebar

import com.harana.designer.frontend.utils.DateUtils
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.designer.ui.components.elements.PrettyDate
import com.harana.ui.components.when
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html._

import java.time.Instant

@react class TextListSection extends StatelessComponent with SidebarSectionComponent {

	case class Props(textListItems: List[TextListItem], showDividers: Boolean = false)

	def render() =
		ul(className := "text-list-group")(
			props.textListItems.zipWithIndex.map { case (textItem, index) =>
				span(key := index.toString)(
					li(className := "list-group-item")(
						h6(className := "list-group-item-heading")(textItem.title),
						span(className := "list-group-item-text")(
							textItem.body match {
								case Some(Left(date)) => PrettyDate(date)
								case Some(Right(string)) => p(string)
								case None => ""
							}
						)
					),
					when(!index.equals(props.textListItems.size -1) && props.showDividers, li(className := "list-group-divider"))
				)
			}
		)
}

case class TextListItem(title: String, body: Option[Either[Instant, String]])