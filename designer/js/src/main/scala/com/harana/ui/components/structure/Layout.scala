package com.harana.ui.components.structure

import com.harana.ui.components.{ColumnSize, Device}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class Layout extends StatelessComponent {
  case class Props(rows: List[Row])

  def render() =
    props.rows.map { row =>
      div(className := "row")(
       row.columnSets.map { columnSet =>
         div(className := columnSetClass(columnSet))(
           columnSet.content
         )
       }
      )
    }

  private def columnSetClass(columnSet: ColumnSet) =
    columnSet.columns.map {
      case (device, column) =>
        s"col-${device.value}-${column.size}" +
          (if (column.offset > 0) s"col-${device.value}-offset-${column.offset}" else "")
    }.mkString(" ")
}

case class Row(columnSets: ColumnSet*)
case class ColumnSet(columns: Map[Device, Column], content: ReactElement)
case class Column(size: ColumnSize, offset: Int = 0)