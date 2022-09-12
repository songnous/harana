package com.harana.ui.external.rowsncolumns.grid

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.react.mod.ReactNode

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@rowsncolumns/grid", "Grid")
@js.native
object ReactGrid extends js.Object

@react object Grid extends ExternalComponent {

  type RowIndex = Int
  type ColumnIndex = Int

  case class Props(columnCount: Int,
                   rowCount: Int,
                   width: js.UndefOr[Number] = js.undefined,
                   height: js.UndefOr[Number] = js.undefined,
                   rowHeight: js.UndefOr[RowIndex => Number],
                   columnWidth: js.UndefOr[ColumnIndex => Number],
                   scrollbarSize: js.UndefOr[Number] = js.undefined,
                   estimatedColumnWidth: js.UndefOr[Number] = js.undefined,
                   estimatedRowHeight: js.UndefOr[Number] = js.undefined,
                   onScroll: js.UndefOr[ScrollCoords => Unit] = js.undefined,
                   onImmediateScroll: js.UndefOr[ScrollCoords => Unit] = js.undefined,
                   showScrollbar: js.UndefOr[Boolean] = js.undefined,
                   activeCell: js.UndefOr[CellInterface] = js.undefined,
                   selectionBackgroundColor: js.UndefOr[String] = js.undefined,
                   selectionBorderColor: js.UndefOr[String] = js.undefined,
                   selectionStrokeWidth: js.UndefOr[Number] = js.undefined,
                   activeCellStrokeWidth: js.UndefOr[Number] = js.undefined,
                   selections: List[SelectionArea] = List(),
                   fillSelection: js.UndefOr[SelectionArea] = js.undefined,
                   mergedCells: List[AreaProps] = List(),
                   frozenRows: js.UndefOr[Int] = js.undefined,
                   frozenColumns: js.UndefOr[Int] = js.undefined,
                   snap: js.UndefOr[Boolean] = js.undefined,
                   showFrozenShadow: js.UndefOr[Boolean] = js.undefined,
                   shadowSettings: js.UndefOr[js.Any] = js.undefined,
                   scrollThrottleTimeout: js.UndefOr[Int] = js.undefined,
                   borderStyles: List[AreaStyle] = List(),
                   cellAreas: js.UndefOr[CellRangeArea] = js.undefined,
                   itemRenderer: js.UndefOr[RendererProps => ReactElement] = js.undefined,
                   overlayRenderer: js.UndefOr[RendererProps => ReactElement] = js.undefined,
                   selectionRenderer: js.UndefOr[RendererProps => ReactElement] = js.undefined,
                   fillHandleProps: js.UndefOr[js.Any => Unit] = js.undefined,
                   onViewChange: js.UndefOr[ViewPortProps => Unit] = js.undefined,
                   onBeforeRenderRow: js.UndefOr[Int => Unit] = js.undefined,
                   children: js.UndefOr[ScrollCoords => ReactElement] = js.undefined,
                   wrapper: js.UndefOr[ReactElement => ReactElement] = js.undefined,
                   showFillHandle: js.UndefOr[Boolean] = js.undefined,
                   overscanCount: js.UndefOr[Int] = js.undefined,
                   fillhandleBorderColor: js.UndefOr[String] = js.undefined,
                   showGridLines: js.UndefOr[Boolean] = js.undefined,
                   gridLineColor: js.UndefOr[String] = js.undefined,
                   gridLineWidth: js.UndefOr[Int] = js.undefined,
                   gridLineRenderer: js.UndefOr[js.Any => ReactNode] = js.undefined,
                   shadowStroke: js.UndefOr[String] = js.undefined,
                   enableCellOverlay: js.UndefOr[Boolean] = js.undefined,
                   isHiddenRow: js.UndefOr[RowIndex => Boolean] = js.undefined,
                   isHiddenColumn: js.UndefOr[ColumnIndex => Boolean] = js.undefined,
                   isHiddenCell: js.UndefOr[(RowIndex, ColumnIndex) => Boolean] = js.undefined,
                   scale: js.UndefOr[Int] = js.undefined)

  override val component = ReactGrid
}