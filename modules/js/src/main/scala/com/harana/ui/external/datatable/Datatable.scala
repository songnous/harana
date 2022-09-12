package com.harana.ui.external.datatable

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportAll, JSImport}

@JSImport("react-bs-datatable", JSImport.Default)
@js.native
object ReactBSDatatable extends js.Object

@react object Datatable extends ExternalComponent {

  case class Props(initialSort: Option[InitialSort] = None,
                   onSort: Option[InitialSort] = None,
                   onFilter: Option[InitialSort] = None,
                   rowsPerPage: Option[Int] = None,
                   rowsPerPageOption: List[Int] = List(),
                   tableHeaders: List[TableHeader],
                   tableBody: List[js.Object],
                   tableClass: Option[String] = None,
                   classes: Option[Classes] = None,
                   labels: Option[Labels] = None)

  override val component = ReactBSDatatable
}

@JSExportAll
case class Async(filterText: String,
                 sortedProp: InitialSort,
                 rowsPerPage: Int,
                 currentPage: Int,
                 maxPage: Int,
                 onSort: String => Unit,
                 onPaginate: Int => Unit,
                 onFilter: String => Unit,
                 onRowsPerPageChange: Int => Unit)

@JSExportAll
case class Classes(controlRow: String,
                   filterCol: String,
                   filterInputGroup: String,
                   filterFormControl: String,
                   filterClearButton: String,
                   paginationOptsCol: String,
                   paginationOptsForm: String,
                   paginationOptsFormGroup: String,
                   paginationOptsFormText: String,
                   paginationOptsFormControl: String,
                   paginationCol: String,
                   paginationButtonGroup: String,
                   paginationButton: String,
                   table: String,
                   thead: String,
                   theadRow: String,
                   theadCol: String,
                   tbody: String,
                   tbodyRow: String,
                   tbodyCol: String)

@JSExportAll
case class InitialSort(prop: String, isAscending: Boolean)

@JSExportAll
case class Labels(first: String,
                  last: String,
                  prev: String,
                  next: String,
                  show: String,
                  entries: String,
                  noResults: String)

@JSExportAll
case class TableHeader(prop: String,
                       //cell:  () => ReactElement = () => Unit,
                       filterable: Boolean = false,
                       sortable: Boolean = false,
                       title: String)