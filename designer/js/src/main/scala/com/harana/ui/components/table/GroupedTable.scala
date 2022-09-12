package com.harana.ui.components.table

import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.ui.components.{ColumnSize, Device, when}
import com.harana.ui.external.shoelace.{Button, Dropdown, Menu, Radio}
import com.harana.ui.external.lazy_load.LazyLoad
import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, React, ReactElement, ReactRef}
import slinky.web.html._

import scala.scalajs.js
import scala.scalajs.js.timers.setTimeout
import scala.util.Random


@react class GroupedTable extends Component {

  case class Props(columns: List[Column], 
                   rowGroups: List[RowGroup],
                   includeRadios: Boolean = true,
                   includeMenus: Boolean = true,
                   className: Option[String] = None)

  case class State(rowClickCount: Int,
                   radioRefs: Map[String, ReactRef[Radio.Def]])

  override def initialState =
    State(0, radioRefs(props.rowGroups))

  override def componentWillReceiveProps(nextProps: Props) =
    if (nextProps.rowGroups != props.rowGroups)
      setState(State(0, radioRefs(nextProps.rowGroups)))

  def radioRefs(rowGroups: List[RowGroup]) =
    rowGroups.zipWithIndex.flatMap { case (group, rowGroupIndex) =>
      group.rows.zipWithIndex.filter(_._1.radio.isDefined).map { case (_, rowIndex) =>
        s"$rowGroupIndex-$rowIndex" -> React.createRef[Radio.Def]
      }
    }.toMap


  val tableId = s"table-${Random.nextInt(10000)}"

  def render() =
    div(className := "panel panel-flat")(
      div(className := props.className.getOrElse("table-responsive"), id := tableId)(
        table(className := "table table-fixed-header text-nowrap")(
          thead(
            tr(
              when(props.includeRadios)(th()),
              props.columns.map { column =>
                th(key := column.title.getOrElse(""), className := columnSizes(column))(when(column.title, column.title.get))
              },
              when(props.includeMenus)(th(className := s"col-md-2")("\u00a0"))
            )
          ),
          tbody(
            props.rowGroups.zipWithIndex.map { case (group, rowGroupIndex) =>
              Fragment(
                when(group.title.isDefined)(tr(className := "active")(
                  td(key := s"$rowGroupIndex-${group.title.get}", colSpan := 5)(group.title.get)
                )),
                group.rows.zipWithIndex.map { case (row, rowIndex) => {
                  val rowId = s"$rowGroupIndex-$rowIndex"

                  tr(key := s"$rowId-tr", onClick := (() => {
                    setState(state.copy(rowClickCount = state.rowClickCount+1), () => {
                      setTimeout(200) {
                        if (state.rowClickCount == 1) if (state.radioRefs.contains(rowId)) state.radioRefs(rowId).current.click()
                        if (state.rowClickCount == 2) if (row.onDoubleClick.isDefined) row.onDoubleClick.get.apply()
                        setState(state.copy(rowClickCount = 0))
                      }
                      ()
                    })
                  }),
                    when(props.includeRadios)(
                      td(key := s"$rowId-radio")(
                        when(row.radio.isDefined)(
                          Radio(row.radio.get.copy(className = Some(s"table-radio-column ${row.radio.get.className.getOrElse("")}"))).withRef(state.radioRefs(rowId))
                        )
                      )
                    ),
                    props.columns.zipWithIndex.map { case (column, columnIndex) =>
                      td(key := s"$rowId-$columnIndex-td", className := columnSizes(column))(row.elements(column))
                    },
                    when(props.includeMenus)(
                      td(key := s"$rowId-menu", className := s"col-md-2")(
                        when(row.menu.isDefined)(
                          LazyLoad(height = 40, offset = 100, unmountIfInvisible = true, scrollContainer = s"#$tableId", scroll = true, children =
                            Dropdown(
                              button = Some(Button.Props(className = Some("list-dropdown"), icon = Some("icomoon","menu7"), slot = Some("trigger"), caret = Some(true))),
                              buttonKey = Some(s"$rowId-menu-btn"),
                              className = Some("inline"),
                              menu = row.menu,
                              hoist = Some(true)
                            ).withKey(s"$rowId-menu")
                          )
                        )
                      )
                    )
                  )
                }}
              ).withKey(rowGroupIndex.toString)
            }
          )
        )
      )
    )

  private def columnSizes(column: Column) = 
    column.size.map { case (device, size) => s"col-${device.value}-${size.value}" }.mkString(" ")
}

case class Column(title: Option[String], size: Map[Device, ColumnSize])
case class RowGroup(title: Option[String], rows: List[Row])
case class Row(elements: Map[Column, ReactElement], radio: Option[Radio.Props] = None, menu: Option[Menu.Props] = None, onDoubleClick: Option[() => Unit] = None)