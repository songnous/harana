package com.harana.ui.external.axui_data_grid

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.std.{KeyboardEvent, MouseEvent}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("axui-datagrid/style.css", JSImport.Default)
@js.native
object DataGridCSS extends js.Object

@js.native
@JSImport("axui-datagrid", "DataGrid")
object ReactDataGrid extends js.Object

@react object DataGrid extends ExternalComponent {

  case class Props(columns: js.Array[Column],
                   width: Int = 1150,
                   height: Int = 800,
                   style: js.UndefOr[js.Any] = js.undefined,
                   options: js.UndefOr[DataGridOptions] = js.undefined,
                   status: js.UndefOr[ReactElement] = js.undefined,
                   autofitColumns: js.UndefOr[Boolean] = js.undefined,
                   onBeforeEvent: js.UndefOr[js.Function1[BeforeAfterEvent, Unit]] = js.undefined,
                   onAfterEvent: js.UndefOr[js.Function1[BeforeAfterEvent, Unit]] = js.undefined,
                   onScrollEnd: js.UndefOr[js.Function1[ScrollEndEvent, Unit]] = js.undefined,
                   onRightClick: js.UndefOr[js.Function1[RightClickEvent, Unit]] = js.undefined,
                   loading: js.UndefOr[Boolean] = js.undefined,
                   loadingData: js.UndefOr[Boolean] = js.undefined,
                   rowSelector: js.UndefOr[RowSelector] = js.undefined)

  override val component = ReactDataGrid
}


trait Column extends js.Object {
  val key: String
  val label: String
  val width: js.UndefOr[Int | String] = js.undefined
  val align: js.UndefOr[String] = js.undefined
  val colSpan: js.UndefOr[Int] = js.undefined
  val rowSpan: js.UndefOr[Int] = js.undefined
  val colIndex: js.UndefOr[Int] = js.undefined
  val rowIndex: js.UndefOr[Int] = js.undefined
  val formatter: js.UndefOr[String] = js.undefined
  val collector: js.UndefOr[String] = js.undefined
  val editor: js.UndefOr[String] = js.undefined
  val hidden: js.UndefOr[Boolean] = js.undefined
  val columns: js.UndefOr[js.Array[Column]] = js.undefined
  val depth: js.UndefOr[Int] = js.undefined
  val columnAttr: js.UndefOr[String] = js.undefined
}

trait BeforeAfterEvent extends js.Object {
  val e: js.UndefOr[MouseEvent | KeyboardEvent]
  val eventName: js.UndefOr[String] = js.undefined
}

trait ScrollEndEvent extends js.Object {
  val endOfScrollTop: js.UndefOr[Boolean] = js.undefined
  val endOfScrollLeft: js.UndefOr[Boolean] = js.undefined
}

trait RightClickEvent extends js.Object {
  val e: MouseEvent
  val item: js.Any
  val value: js.Any
  val usedRow: js.UndefOr[Int] = js.undefined
  val focusedCol: js.UndefOr[Int] = js.undefined
}

trait RowSelector extends js.Object {
  val show: js.UndefOr[Boolean] = js.undefined
  val rowKey: js.UndefOr[String] = js.undefined
  val selectedRowKeys: js.UndefOr[Array[String]] = js.undefined
  val onChange: js.UndefOr[js.Function1[js.Any, Unit]] = js.undefined
}

trait ColumnKeys extends js.Object {
  val selected: js.UndefOr[String] = js.undefined
  val modified: js.UndefOr[String] = js.undefined
  val deleted: js.UndefOr[String] = js.undefined
  val disableSelection: js.UndefOr[String] = js.undefined
}

trait Header extends js.Object {
  val display: js.UndefOr[Boolean] = js.undefined
  val align: js.UndefOr[String] = js.undefined
  val columnHeight: js.UndefOr[Int] = js.undefined
  val columnPadding: js.UndefOr[Int] = js.undefined
  val columnBorderWidth: js.UndefOr[Int] = js.undefined
  val selector: js.UndefOr[Boolean] = js.undefined
  val sortable: js.UndefOr[Boolean] = js.undefined
  val enableFilter: js.UndefOr[Boolean] = js.undefined
  val clickAction: js.UndefOr[String] = js.undefined
}

trait Body extends js.Object {
  val align: js.UndefOr[String] = js.undefined
  val columnHeight: js.UndefOr[Int] = js.undefined
  val columnPadding: js.UndefOr[Int] = js.undefined
  val columnBorderWidth: js.UndefOr[Int] = js.undefined
  val grouping: js.UndefOr[Boolean] = js.undefined
  val mergeCells: js.UndefOr[Boolean] = js.undefined
}

trait Page extends js.Object {
  val height: js.UndefOr[Int] = js.undefined
}

trait Scroller extends js.Object {
  val size: js.UndefOr[Int] = js.undefined
  val arrowSize: js.UndefOr[Int] = js.undefined
  val barMinSize: js.UndefOr[Int] = js.undefined
  val padding: js.UndefOr[Int] = js.undefined
  val disabledVerticalScroll: js.UndefOr[Boolean] = js.undefined
}

trait DataGridOptions extends js.Object {
  val frozenColumnIndex: js.UndefOr[Int] = js.undefined
  val frozenRowIndex: js.UndefOr[Int] = js.undefined
  val showLineNumber: js.UndefOr[Boolean] = js.undefined
  val showRowSelector: js.UndefOr[Boolean] = js.undefined
  val multipleSelect: js.UndefOr[Boolean] = js.undefined
  val columnMinWidth: js.UndefOr[Int] = js.undefined
  val lineNumberColumnWidth: js.UndefOr[Int] = js.undefined
  val rowSelectorColumnWidth: js.UndefOr[Int] = js.undefined
  val remoteSort: js.UndefOr[Boolean] = js.undefined
  val asidePanelWidth: js.UndefOr[Int] = js.undefined
  val header: js.UndefOr[Header] = js.undefined
  val body: js.UndefOr[Body] = js.undefined
  val page: js.UndefOr[Page] = js.undefined
  val scroller: js.UndefOr[Scroller] = js.undefined
  val columnKeys: js.UndefOr[ColumnKeys] = js.undefined
  val bodyLoaderHeight: js.UndefOr[Int] = js.undefined
}