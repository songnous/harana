package com.harana.ui.external.json_view

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-json-view", JSImport.Default)
@js.native
object ReactJsonView extends js.Object

@react object JsonView extends ExternalComponent {

  case class Props(src: js.Any,
                   name: String | Boolean = false,
                   theme: String = "rjv-default",
                   style: Option[js.Object] = None,
                   iconStyle: Option[String] = None,
                   indentWidth: Option[Int] = None,
                   collapsed: Option[Boolean | Int] = None,
                   collapseStringsAfterLength: Option[Boolean] = None,
                   shouldCollapse: Option[Field => Unit] = None,
                   groupArraysAfterLength: Option[Int] = None,
                   enableClipboard: Option[Boolean | String => Unit] = None,
                   displayObjectSize: Option[Boolean] = None,
                   displayDataTypes: Option[Boolean] = None,
                   defaultValue: Option[js.Any] = None,
                   onEdit: Option[Change => Unit] = None,
                   onAdd: Option[Change => Unit] = None,
                   onDelete: Option[Change => Unit] = None,
                   onSelect: Option[Change => Unit] = None,
                   sortKeys: Option[Boolean] = None,
                   validationMessage: Option[String] = None)

  override val component = ReactJsonView
}

case class Change(updated_src: String, name: String, namespace: String, new_value: String, existing_value: String)
case class Field(name: String, src: String, `type`: String, namespace: String)