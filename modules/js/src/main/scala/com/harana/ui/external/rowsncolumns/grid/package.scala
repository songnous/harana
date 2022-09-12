package com.harana.ui.external.rowsncolumns

import typings.react.mod.Key
import typings.std.Record

import scala.scalajs.js
import scala.scalajs.js.|

package object grid {

  type Number = Int | Double
  type CellMetaDataMap = Record[Number, CellMetaData]
  
  trait CellPosition extends js.Object {
    val x: Number
    val y: Number
    val width: Number
    val height: Number
  }
  
  trait RendererProps extends CellInterface with CellPosition {
    val key: Key
    val isMergedCell: js.UndefOr[Boolean] = js.undefined
    val isOverlay: js.UndefOr[Boolean] = js.undefined
  }
  
  trait CellRangeArea extends CellInterface {
    val toColumnIndex: Int
  }
  
  trait ScrollCoords extends js.Object {
    val scrollTop: Int
    val scrollLeft: Int
  }
  
  trait OptionalScrollCoords extends js.Object {
    val scrollTop: js.UndefOr[Int] = js.undefined
    val scrollLeft: js.UndefOr[Int] = js.undefined
  }
  
  trait ScrollState extends ScrollCoords {
    val isScrolling: Boolean
    val verticalScrollDirection: String
    val horizontalScrollDirection: String
  }
  
  trait SelectionArea extends AreaStyle {
    val inProgress: Boolean
    val isFilling: Boolean
  }
  
  trait AreaProps extends js.Object {
    val top: Int
    val bottom: Int
    val left: Int
    val right: Int
  }
  
  trait CellInterface extends js.Object {
    val rowIndex: Int
    val columnIndex: Int
  }
  
  trait OptionalCellInterface extends js.Object {
    val rowIndex: js.UndefOr[Int] = js.undefined
    val columnIndex: js.UndefOr[Int] = js.undefined
  }
  
  trait ViewPortProps extends js.Object {
    val rowStartIndex: Int
    val rowStopIndex: Int
    val columnStartIndex: Int
    val columnStopIndex: Int
    val visibleRowStartIndex: Int
    val visibleRowStopIndex: Int
    val visibleColumnStartIndex: Int
    val visibleColumnStopIndex: Int
  }
  
  trait InstanceInterface extends js.Object {
    val columnMetadataMap: CellMetaDataMap
    val rowMetadataMap: CellMetaDataMap
    val lastMeasuredColumnIndex: Int
    val lastMeasuredRowIndex: Int
    val estimatedRowHeight: Int
    val estimatedColumnWidth: Int
    val recalcColumnIndices: js.Array[Int]
    val recalcRowIndices: js.Array[Int]
  }
  
  trait CellMetaData extends js.Object {
    val offset: Int
    val size: Int
  }
  
  trait SnapRowProps extends js.Object {
    val deltaY: Int
  }
  
  trait SnapColumnProps extends js.Object {
    val deltaX: Int
  }
  
  trait PosXY extends js.Object {
    val x: js.UndefOr[Int] = js.undefined
    val y: js.UndefOr[Int] = js.undefined
  }
  
  trait PosXYRequired extends js.Object {
    val x: Int
    val y: Int
  }
  
  trait AreaStyle extends js.Object  {
    val bounds: js.UndefOr[AreaProps] = js.undefined
    val style: js.UndefOr[Style] = js.undefined
  }
  
  trait Style extends js.Object {
    val stroke: js.UndefOr[String] = js.undefined
    val strokeLeftColor: js.UndefOr[String] = js.undefined
    val strokeTopColor: js.UndefOr[String] = js.undefined
    val strokeRightColor: js.UndefOr[String] = js.undefined
    val strokeBottomColor: js.UndefOr[String] = js.undefined
    val strokeWidth: js.UndefOr[Int] = js.undefined
    val strokeTopWidth: js.UndefOr[Int] = js.undefined
    val strokeRightWidth: js.UndefOr[Int] = js.undefined
    val strokeBottomWidth: js.UndefOr[Int] = js.undefined
    val strokeLeftWidth: js.UndefOr[Int] = js.undefined
    val strokeStyle: js.UndefOr[String] = js.undefined
  }
  
  trait ScrollSnapRef extends js.Object {
    val visibleRowStartIndex: Int
    val rowCount: Int
    val frozenRows: Int
    val visibleColumnStartIndex: Int
    val columnCount: Int
    val frozenColumns: Int
    val isHiddenRow: js.UndefOr[Int => Boolean] = js.undefined
    val isHiddenColumn: js.UndefOr[Int => Boolean] = js.undefined
  }
}