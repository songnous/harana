package com.harana.ui.external.data_grid

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.react.mod.{CSSProperties, SVGAttributes}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@js.native
@JSImport("react-data-grid", JSImport.Default)
object ReactDataGrid extends js.Object

@react object DataGrid extends ExternalComponent {

  case class Props(columns: List[Column],
                   dataSource: List[js.Any] | String | (PageSize => js.Promise[List[js.Any]]) = "",
                   dataSourceCount: Option[Double] = None,
                   defaultPage: Option[Double] = None,
                   defaultPageSize: Option[Double] = None,
                   emptyText: Option[String] = None,
                   groupBy: List[js.Any] = List(),
                   idProperty: String = "id",
                   liveFilter: Option[Boolean] = None,
                   loadMaskOverHeader: Option[Boolean] = None,
                   loading: Option[Boolean] = None,
                   onColumnOrderChange: Option[(Double, Double) => Unit] = None,
                   onColumnResize: Option[(Column, Double, Column, Double) => Unit] = None,
                   onColumnVisibilityChange: Option[(Column, Boolean) => Unit] = None,
                   onFilter: Option[(Column, js.Any, List[js.Any]) => Unit] = None,
                   onPageChange: Option[Double => Unit] = None,
                   onPageSizeChange: Option[(Double, js.Object) => Unit] = None,
                   onSelectionChange: Option[(js.Object, js.Any) => Unit] = None,
                   onSortChange: Option[List[SortInfo] => Unit] = None,
                   page: Option[Double] = None,
                   pageSize: Option[Double] = None,
                   pagination: Option[Boolean] = None,
                   paginationToolbarProps: Option[PaginationToolbarProps] = None,
                   rowHeight: Option[Double] = None,
                   rowStyle: Option[CSSProperties | ((js.Any, RowProps) => CSSProperties)] = None,
                   selected: Option[js.Object] = None,
                   showCellBorders: Option[Boolean | String] = None,
                   sortInfo: List[SortInfo] = List(),
                   style: Option[CSSProperties] = None,
                   withColumnMenu: Option[Boolean] = None)

  override val component = ReactDataGrid
}

case class CellProps(className: String,
                     index: Double,
                     rowIndex: Double,
                     style: CSSProperties)

case class Column(name: String,
                  className: Option[String] = None,
                  defaultHidden: Option[Boolean] = None,
                  defaultVisible: Option[Boolean] = None,
                  flex: Option[Double] = None,
                  minWidth: Option[Double] = None,
                  render: Option[js.Function3[js.Any, js.Any, CellProps, _]] = None,
                  style: Option[CSSProperties] = None,
                  textAlign: Option[String] = None,
                  title: Option[String | ReactElement] = None,
                  visible: Option[Boolean] = None,
                  width: Option[Double] = None)

case class DisabledStyle(disabledStyle: SVGAttributes[js.Object],
                         overStyle: SVGAttributes[js.Object],
                         style: SVGAttributes[js.Object])

case class PageSize(pageSize: Double, skip: Double)

case class PaginationToolbarProps(iconProps: DisabledStyle,
                                  iconSize: Double,
                                  pageSizes: List[Double],
                                  showPageSize: Boolean,
                                  showRefreshIcon: Boolean)

case class RowProps(className: String,
                    index: Double,
                    overClassName: String,
                    selectedClassName: String)

case class SortInfo(dir: String, name: String)