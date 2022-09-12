package com.harana.ui.external.json_view2

import com.harana.ui.external.json_view2.Types._
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-json-view", JSImport.Namespace)
@js.native
object ReactJsonView extends js.Object

@react object JsonView extends ExternalComponent {

  case class Props(collapseStringsAfterLength: Option[Double | Boolean] = None,
                   collapsed: Option[Boolean | Double] = None,
                   defaultValue: Option[TypeDefaultValue | List[TypeDefaultValue] | Null] = None,
                   displayDataTypes: Option[Boolean] = None,
                   displayObjectSize: Option[Boolean] = None,
                   enableClipboard: Option[Boolean | OnCopyProps => Unit] = None,
                   groupArraysAfterLength: Option[Double] = None,
                   iconStyle: Option[String] = None,
                   indentWidth: Option[Double] = None,
                   name: Option[String | Null] = None,
                   onAdd: Option[InteractionProps => Boolean | Boolean] = None,
                   onDelete: Option[InteractionProps => Boolean | Boolean] = None,
                   onEdit: Option[InteractionProps => Boolean | Boolean] = None,
                   onSelect: Option[OnSelectProps => Unit | Boolean] = None,
                   shouldCollapse: Option[Boolean | (CollapsedFieldProps => Boolean)] = None,
                   sortKeys: Option[Boolean] = None,
                   src: js.Object,
                   style: Option[CSSProperties] = None,
                   theme: Option[ThemeKeys | ThemeObject] = None,
                   validationMessage: Option[String] = None)

  override val component = ReactJsonView
}

object Types {
  type ThemeKeys = String
  type TypeDefaultValue = String | Double | Boolean | js.Object
}

case class CollapsedFieldProps(name: String, namespace: List[String], src: js.Object, `type`: String)

case class InteractionProps(existing_src: js.Object,
                            existing_value: js.Object | String | Double | Boolean,
                            name: String,
                            namespace: List[String],
                            new_value: Option[js.Object | String | Double | Boolean] = None,
                            updated_src: js.Object)

case class OnCopyProps(name: String, namespace: List[String], src: js.Object)
case class OnSelectProps(name: String, namespace: List[String], `type`: String, value: js.Object | String | Double | Boolean)

case class ThemeObject(base00: String,
                       base01: String,
                       base02: String,
                       base03: String,
                       base04: String,
                       base05: String,
                       base06: String,
                       base07: String,
                       base08: String,
                       base09: String,
                       base0A: String,
                       base0B: String,
                       base0C: String,
                       base0D: String,
                       base0E: String,
                       base0F: String)