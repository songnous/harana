package com.harana.ui.external.typeahead

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-bootstrap-typeahead", JSImport.Default)
@js.native
object ReactBootstrapTypeahead extends js.Object

@react object Typeahead extends ExternalComponent {

  case class Props(options: List[TypeaheadOptions],
                   a11yNumResults: Option[() => Unit] = None,
                   a11yNumSelected: Option[() => Unit] = None,
                   align: Option[String] = None,
                   allowNew: Option[Boolean] = None,
                   autoFocus: Option[Boolean] = None,
                   bodyContainer: Option[Boolean] = None,
                   caseSensitive: Option[Boolean] = None,
                   clearButton: Option[Boolean] = None,
                   defaultInputValue: Option[String] = None,
                   defaultOpen: Option[Boolean] = None,
                   defaultSelected: List[String] = List(),
                   disabled: Option[Boolean] = None,
                   dropup: Option[Boolean] = None,
                   emptyLabel: Option[String] = None,
                   filterBy: Option[String] = None,
                   flip: Option[Boolean] = None,
                   highlightOnlyResult: Option[Boolean] = None,
                   id: Option[String] = None,
                   ignoreDiacritics: Option[Boolean] = None,
                   inputProps: js.Object,
                   isInvalid: Option[Boolean] = None,
                   isLoading: Option[Boolean] = None,
                   isValid: Option[Boolean] = None,
                   labelKey: Option[String] = None,
                   maxResults: Option[Int] = None,
                   menuId: Option[String] = None,
                   minLength: Option[Int] = None,
                   multiple: Option[Boolean] = None,
                   onBlur: Option[() => Unit] = None,
                   onChange: Option[() => Unit] = None,
                   onFocus: Option[() => Unit] = None,
                   onInputChange: Option[() => Unit] = None,
                   onKeyDown: Option[() => Unit] = None,
                   onMenuToggle: Option[() => Unit] = None,
                   onPaginate: Option[() => Unit] = None,
                   open: Option[Boolean] = None,
                   paginate: Option[Boolean] = None,
                   paginationText: Option[String] = None,
                   placeholder: Option[String] = None,
                   renderMenu: Option[() => Unit] = None,
                   selected: List[String] = List(),
                   selectHintOnEnter: Option[Boolean] = None)

  override val component = ReactBootstrapTypeahead
}

@js.native
trait TypeaheadOptions extends js.Object {
  val id: String = js.native
  val label: String = js.native
}