package com.harana.designer.frontend.files.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.files.FilesStore.{PushPath, State, UpdateSelectedFile}
import com.harana.designer.frontend.utils.SizeUtils
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.designer.ui.components.elements.PrettyDate
import com.harana.ui.components.ColumnSize
import com.harana.ui.components.Device.Desktop
import com.harana.ui.components.elements.DataFileName
import com.harana.ui.components.table.{Column, Row}
import com.harana.ui.external.shoelace.Radio
import slinky.core.facade.ReactElement
import slinky.web.html.p

object table {

  val columns = List(
    Column(Some(i"files.table.column.name"), Map(Desktop -> ColumnSize.Five)),
    Column(Some(i"files.table.column.size"), Map(Desktop -> ColumnSize.Two)),
    Column(Some(i"files.table.column.updated"), Map(Desktop -> ColumnSize.Two)),
    Column(Some(i"files.table.column.tags"), Map(Desktop -> ColumnSize.Three))
  )

  val selectColumns = List(
    Column(Some(i"files.table.column.name"), Map(Desktop -> ColumnSize.Six)),
    Column(Some(i"files.table.column.size"), Map(Desktop -> ColumnSize.Three)),
    Column(Some(i"files.table.column.updated"), Map(Desktop -> ColumnSize.Three))
  )

  def rows(state: State): List[Row] =
    state.files.zipWithIndex.map { case (file, index) =>
      val selected = state.selectedFile.nonEmpty && state.selectedFile.get.equals(file)

      Row(
        Map[Column, ReactElement](
          columns.head -> DataFileName(file).withKey(s"datafilename-$index"),
          columns(1) -> p(SizeUtils.format(file.size)),
          columns(2) -> PrettyDate(file.updated),
          columns(3) -> ""
        ),
        radio = Some(Radio.Props(name = "s", checked = Some(selected), onChange = Some(value => if (value) Circuit.dispatch(UpdateSelectedFile(Some(file)))))),
        onDoubleClick = Some(() => Circuit.dispatch(PushPath(file.name)))
      )
    }

  def selectRows(state: State): List[Row] =
    state.files.zipWithIndex.map { case (file, index) =>
      val selected = state.selectedFile.nonEmpty && state.selectedFile.get.equals(file)

      Row(
        Map[Column, ReactElement](
          selectColumns.head -> DataFileName(file).withKey(s"datafilename-$index"),
          selectColumns(1) -> p(SizeUtils.format(file.size)),
          selectColumns(2) -> PrettyDate(file.updated)
        ),
        onDoubleClick = Some(() => Circuit.dispatch(PushPath(file.name))),
        radio =
          if (file.isFolder)
            Some(Radio.Props(name = "s", checked = Some(selected), onChange = Some(value => if (value) Circuit.dispatch(UpdateSelectedFile(Some(file))))))
          else
            None
      )
    }
}
