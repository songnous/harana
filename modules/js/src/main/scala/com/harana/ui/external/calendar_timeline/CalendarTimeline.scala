package com.harana.ui.external.calendar_timeline

import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{ExternalComponent, ReactComponentClass}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-calendar-timeline", JSImport.Default)
@js.native
object ReactCalendarTimeline extends js.Object {
  val default: ReactComponentClass[CalendarTimeline.Props] = js.native
  val defaultHeaderLabelFormats: TimelineHeaderLabelFormat = js.native
  val defaultSubHeaderLabelFormats: TimelineHeaderLabelFormat = js.native
}

@react object CalendarTimeline extends ExternalComponent {

  case class Props(canChangeGroup: Option[Boolean] = None,
                   canMove: Option[Boolean] = None,
                   canResize: Option[Boolean | String] = None,
                   children: Option[js.Any] = None,
                   clickTolerance: Option[Double] = None,
                   defaultTimeEnd: Option[js.Any] = None,
                   defaultTimeStart: Option[js.Any] = None,
                   dragSnap: Option[Double] = None,
                   groupRenderer: Option[Group => ReactElement] = None,
                   groups: List[TimelineGroup],
                   headerLabelFormats: Option[TimelineHeaderLabelFormat] = None,
                   headerLabelGroupHeight: Option[Double] = None,
                   headerLabelHeight: Option[Double] = None,
                   headerRef: Option[js.Any] = None,
                   itemHeightRatio: Option[Double] = None,
                   itemRenderer: Option[Context => ReactElement] = None,
                   itemTouchSendsClick: Option[Boolean] = None,
                   items: List[TimelineItem],
                   keys: Option[GroupIdKey] = None,
                   lineHeight: Option[Double] = None,
                   maxZoom: Option[Double] = None,
                   minResizeWidth: Option[Double] = None,
                   minZoom: Option[Double] = None,
                   minimumWidthForItemContentVisibility: Option[Double] = None,
                   moveResizeValidator: Option[(String, Double, Double, String) => Unit] = None,
                   onBoundsChange: Option[(Double, Double) => Unit] = None,
                   onCanvasClick: Option[(Double, Double, js.Any) => Unit] = None,
                   onCanvasContextMenu: Option[(TimelineGroup, Double, js.Any) => Unit] = None,
                   onCanvasDoubleClick: Option[(TimelineGroup, Double, js.Any) => Unit] = None,
                   onItemClick: Option[(Double, js.Any, Double) => Unit] = None,
                   onItemDoubleClick: Option[(Double, js.Any, Double) => Unit] = None,
                   onItemMove: Option[(Double, Double, Double) => Unit] = None,
                   onItemResize: Option[(Double, Double, String) => Unit] = None,
                   onItemSelect: Option[(Double, js.Any, Double) => Unit] = None,
                   onTimeChange: Option[(Double, Double, (Double, Double) => Unit) => Unit] = None,
                   onTimeInit: Option[(Double, Double) => Unit] = None,
                   onZoom: Option[TimelineContext => Unit] = None,
                   rightSidebarContent: Option[ReactElement] = None,
                   rightSidebarWidth: Option[Double] = None,
                   selected: List[Double] = List(),
                   showCursorLine: Option[Boolean] = None,
                   sidebarContent: Option[ReactElement] = None,
                   sidebarWidth: Option[Double] = None,
                   stackItems: Option[Boolean] = None,
                   stickyHeader: Option[Boolean] = None,
                   stickyOffset: Option[Double] = None,
                   subHeaderLabelFormats: Option[TimelineHeaderLabelFormat] = None,
                   timeSteps: Option[TimelineTimeSteps] = None,
                   traditionalZoom: Option[Boolean] = None,
                   useResizeHandle: Option[Boolean] = None,
                   visibleTimeEnd: Option[js.Any] = None,
                   visibleTimeStart: Option[js.Any] = None
  )

  override val component = ReactCalendarTimeline
}

case class Context(context: TimelineContext, item: TimelineItem)
case class Group(group: TimelineGroup, isRightSidebar: Boolean)

case class GroupIdKey(groupIdKey: String,
                      groupTitleKey: String,
                      itemGroupKey: String,
                      itemIdKey: String,
                      itemTimeEndKey: String,
                      itemTimeStartKey: String,
                      itemTitleKey: String)

case class TimelineContext(timelineWidth: Double, visibleTimeEnd: Double, visibleTimeStart: Double)
case class TimelineGroup(id: Double, rightTitle: Option[ReactElement], title: ReactElement)

case class TimelineHeaderLabelFormat(dayLong: String,
                                     dayShort: String,
                                     hourLong: String,
                                     hourMedium: String,
                                     hourMediumLong: String,
                                     hourShort: String,
                                     monthLong: String,
                                     monthMedium: String,
                                     monthMediumLong: String,
                                     monthShort: String,
                                     time: String,
                                     yearLong: String,
                                     yearShort: String)

case class TimelineItem(canChangeGroup: Option[Boolean],
                        canMove: Option[Boolean],
                        canResize: Option[Boolean | String],
                        className: Option[String],
                        end_time: js.Any,
                        group: Double,
                        id: Double,
                        itemProps: Option[js.Object],
                        start_time: js.Any,
                        title: Option[ReactElement])

case class TimelineTimeSteps(day: Double,
                             hour: Double,
                             minute: Double,
                             month: Double,
                             second: Double,
                             year: Double)