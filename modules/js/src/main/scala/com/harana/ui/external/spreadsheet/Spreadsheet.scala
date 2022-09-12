package com.harana.ui.external.spreadsheet

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-spreadsheet", JSImport.Default)
@js.native
object ReactSpreadsheet extends js.Object

@react object Spreadsheet extends ExternalComponent {

  case class Props(data: List[List[Value]],
                   columnLabels: List[String] = List(),
                   ColumnIndicator: Option[ReactElement] = None,
                   rowLabels: List[String] = List(),
                   RowIndicator: Option[ReactElement] = None,
                   hideColumnIndicators: Option[Boolean] = None,
                   hideRowIndicators: Option[Boolean] = None,
                   Table: Option[ReactElement] = None,
                   Row: Option[ReactElement] = None,
                   Cell: Option[ReactElement] = None,
                   DataViewer: Option[ReactElement] = None,
                   DataEditor: Option[ReactElement] = None,
                   getValue: Option[ReactElement] = None,
                   getBindingsForCell: Option[ReactElement] = None,
                   store: Option[js.Any] = None)

  override val component = ReactSpreadsheet
}

case class Value(value: String)