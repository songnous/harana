package com.harana.ui.components.structure

import com.harana.ui.components._
import com.harana.ui.components.ColumnSize
import enumeratum.values._
import slinky.core.{StatelessComponent}
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

import scala.math.BigDecimal.RoundingMode

@react class Grid extends StatelessComponent {

  case class Props(items: List[ReactElement], columns: ColumnSize)

  def render() = {
    val rowCount = BigDecimal(props.items.size.toFloat / props.columns.value).setScale(0, RoundingMode.UP).toInt
    var counter = -1

    (1 to rowCount).map { rowIndex =>
      div(className := "row", key := s"row-$rowIndex")(
        (1 to props.columns.value).map { columnIndex =>
          div(className := s"col-md-${12/props.columns.value}", key := s"row-$rowIndex-col-$columnIndex") {
            if (counter < props.items.size - 1) {
              counter = counter + 1
              props.items(counter)
            }else{
              emptyElement
            }
          }
        }
      )
    }
  }
}