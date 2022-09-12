package com.harana.ui.external.rowsncolumns

import com.harana.ui.external.rowsncolumns.grid._
import typings.react.mod.ReactText
import typings.std.Record

import scala.scalajs.js
import scala.scalajs.js.|

package object spreadsheet {

  type Cell = Record[String, CellConfig]
  type Cells = Record[String, Cell]
  type ContainsTextOperators = String
  type DataType = String
  type DataValidationOperator = String
  type DataValidationType = String
  type EditorType = String
  type Filter = Record[String, FilterDefinition]
  type FormatInputValue = js.UndefOr[String | Double | Boolean | js.Date]
  type Formatter = (FormatInputValue, DataType, CellDataFormatting) => js.UndefOr[String]
  type Functions = Record[String, js.Any => Unit]
  type Number = Int | Double | Float
  type SizeType = js.Dictionary[Int]
  type SelectionMode = String
  type SelectionPolicy = String
  type TooltipPosition = String
  type TooltipVariant = String
  type Wrap = String

  trait CellDataFormatting extends js.Object {
    val percent: js.UndefOr[Boolean] = js.undefined
    val currencySymbol: js.UndefOr[String] = js.undefined
    val format: js.UndefOr[String] = js.undefined
  }

  trait CellFormatting extends CellDataFormatting {
    val datatype: js.UndefOr[DataType] = js.undefined
    val formulatype: js.UndefOr[DataType] = js.undefined
    val formulaRange: js.UndefOr[js.Array[Int | Double]] = js.undefined
    val parentCell: js.UndefOr[String] = js.undefined
    val loading: js.UndefOr[Boolean] = js.undefined
    val loadingText: js.UndefOr[String] = js.undefined
    val plaintext: js.UndefOr[Boolean] = js.undefined
    val bold: js.UndefOr[Boolean] = js.undefined
    val color: js.UndefOr[String] = js.undefined
    val italic: js.UndefOr[Boolean] = js.undefined
    val horizontalAlign: js.UndefOr[String] = js.undefined
    val verticalAlign: js.UndefOr[String] = js.undefined
    val underline: js.UndefOr[Boolean] = js.undefined
    val strike: js.UndefOr[Boolean] = js.undefined
    val fill: js.UndefOr[String] = js.undefined
    val stroke: js.UndefOr[String] = js.undefined
    val strokeTopColor: js.UndefOr[String] = js.undefined
    val strokeRightColor: js.UndefOr[String] = js.undefined
    val strokeBottomColor: js.UndefOr[String] = js.undefined
    val strokeLeftColor: js.UndefOr[String] = js.undefined
    val strokeWidth: js.UndefOr[Number] = js.undefined
    val strokeTopWidth: js.UndefOr[Number] = js.undefined
    val strokeRightWidth: js.UndefOr[Number] = js.undefined
    val strokeBottomWidth: js.UndefOr[Number] = js.undefined
    val strokeLeftWidth: js.UndefOr[Number] = js.undefined
    val strokeDash: js.UndefOr[js.Array[Number]] = js.undefined
    val strokeTopDash: js.UndefOr[js.Array[Number]] = js.undefined
    val strokeRightDash: js.UndefOr[js.Array[Number]] = js.undefined
    val strokeBottomDash: js.UndefOr[js.Array[Number]] = js.undefined
    val strokeLeftDash: js.UndefOr[js.Array[Number]] = js.undefined
    val lineCap: js.UndefOr[String] = js.undefined
    val padding: js.UndefOr[Number] = js.undefined
    val fontSize: js.UndefOr[Number] = js.undefined
    val fontFamily: js.UndefOr[String] = js.undefined
    val locked: js.UndefOr[Boolean] = js.undefined
    val hidden: js.UndefOr[Boolean] = js.undefined
    val wrap: js.UndefOr[Wrap] = js.undefined
    val rotation: js.UndefOr[Number] = js.undefined
    val dataValidation: js.UndefOr[DataValidation] = js.undefined
    val hyperlink: js.UndefOr[String] = js.undefined
    val image: js.UndefOr[String] = js.undefined
  }

  trait CellConfig extends CellFormatting {
    val text: js.UndefOr[String | Int] = js.undefined
    val tooltip: js.UndefOr[String] = js.undefined
    val result: js.UndefOr[String | Int | Boolean | js.Date] = js.undefined
    val error: js.UndefOr[String] = js.undefined
    val valid: js.UndefOr[Boolean] = js.undefined
  }

  trait DataValidation extends js.Object {
    val `type`: DataValidationType
    val formulae: js.UndefOr[js.Any] = js.undefined
    val allowBlank: js.UndefOr[Boolean] = js.undefined
    val operator: js.UndefOr[DataValidationOperator] = js.undefined
    val error: js.UndefOr[String] = js.undefined
    val errorTitle: js.UndefOr[String] = js.undefined
    val errorStyle: js.UndefOr[String] = js.undefined
    val prompt: js.UndefOr[String] = js.undefined
    val promptTitle: js.UndefOr[String] = js.undefined
    val showErrorMessage: js.UndefOr[Boolean] = js.undefined
    val showInputMessage: js.UndefOr[Boolean] = js.undefined
  }

  trait FilterDefinition extends js.Object {
    val operator: ContainsTextOperators | DataValidationOperator
    val values: js.Array[js.Any]
  }

  trait FilterView extends js.Object {
    val bounds: AreaProps
    val filters: js.UndefOr[Filter] = js.undefined
  }

  trait Sheet extends js.Object {
    val id: SheetID
    val name: String
    val cells: Cells
    val activeCell: js.UndefOr[CellInterface] = js.undefined
    val selections: js.UndefOr[Array[SelectionArea]] = js.undefined
    val scrollState: js.UndefOr[ScrollCoords] = js.undefined
    val columnSizes: js.UndefOr[SizeType] = js.undefined
    val rowSizes: js.UndefOr[SizeType] = js.undefined
    val mergedCells: js.UndefOr[AreaProps] = js.undefined
    val frozenRows: js.UndefOr[Number] = js.undefined
    val frozenColumns: js.UndefOr[Number] = js.undefined
    val hiddenRows: js.UndefOr[Array[Number]] = js.undefined
    val hiddenColumns: js.UndefOr[Array[Number]] = js.undefined
    val showGridLines: js.UndefOr[Boolean] = js.undefined
    val filterViews: js.UndefOr[Array[FilterView]] = js.undefined
    val rowCount: js.UndefOr[Boolean] = js.undefined
    val columnCount: js.UndefOr[Number] = js.undefined
    val locked: js.UndefOr[Boolean] = js.undefined
    val hidden: js.UndefOr[Boolean] = js.undefined
    val tabColor: js.UndefOr[String] = js.undefined
  }

  trait StateInterface extends js.Object {
    val selectedSheet: js.UndefOr[ReactText] = js.undefined
    val sheets: js.UndefOr[Array[Sheet]] = js.undefined
    val currentActiveCell: js.UndefOr[CellInterface] = js.undefined
    val currentSelections: js.UndefOr[Array[SelectionArea]] = js.undefined
  }
}