package com.harana.ui.external.autosuggest

import org.scalajs.dom.HTMLInputElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-bootstrap-autosuggest", "Autosuggest")
@js.native
object ReactBootstrapAutosuggest extends js.Object

@react object Autosuggest extends ExternalComponent {

  case class Props(addonAfter: Option[ReactElement] = None,
                   addonBefore: Option[ReactElement] = None,
                   allowDuplicates: Option[Boolean] = None,
                   bsSize: Option[String] = None,
                   buttonAfter: Option[ReactElement] = None,
                   buttonBefore: Option[ReactElement] = None,
                   choicesClass: Option[String] = None,
                   closeOnCompletion: Option[Boolean] = None,
                   datalist: Option[js.Any] = None,
                   datalistAdapter: Option[js.Any] = None,
                   datalistMessage: Option[String] = None,
                   datalistOnly: Option[Boolean] = None,
                   datalistPartial: Option[Boolean] = None,
                   defaultValue: Option[js.Any] = None,
                   disabled: Option[Boolean] = None,
                   dropup: Option[Boolean] = None,
                   groupClassName: Option[String] = None,
                   inputSelect: Option[(HTMLInputElement, String, String) => Unit] = None,
                   itemAdapter: Option[js.Any] = None,
                   itemReactKeyPropName: Option[String] = None,
                   itemSortKeyPropName: Option[String] = None,
                   itemValuePropName: Option[String] = None,
                   multiple: Option[Boolean] = None,
                   onAdd: Option[js.Any => Unit] = None,
                   onBlur: Option[js.Any => Unit] = None,
                   onChange: Option[js.Any => Unit] = None,
                   onDatalistMessageSelect: Option[() => Unit] = None,
                   onFocus: Option[js.Any => Unit] = None,
                   onRemove: Option[Int => Unit] = None,
                   onSearch: Option[String => Unit] = None,
                   onSelect: Option[js.Any => Unit] = None,
                   onToggle: Option[Boolean => Unit] = None,
                   placeholder: Option[String] = None,
                   required: Option[Boolean] = None,
                   searchDebounce: Option[Int] = None,
                   showToggle: Option[Boolean] = None,
                   suggestionsClass: Option[String] = None,
                   toggleId: Option[String] = None,
                   `type`: Option[String] = None,
                   value: Option[js.Any] = None,
                   valueIsItem: Option[Boolean] = None)

  override val component = ReactBootstrapAutosuggest
}