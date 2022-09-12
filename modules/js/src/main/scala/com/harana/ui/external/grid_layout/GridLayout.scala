package com.harana.ui.external.grid_layout

import com.harana.ui.external.grid_layout.Types._
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.HTMLElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-grid-layout", "ReactGridLayout")
@js.native
object ReactGridLayout extends js.Object

@react object GridLayout extends ExternalComponent {

  case class Props(width: Int,
                   autoSize: Option[Boolean] = None,
                   cols: Option[Int] = None,
                   draggableCancel: Option[String] = None,
                   draggableHandle: Option[String] = None,
                   verticalCancel: Option[Boolean] = None,
                   compactType: Option[String] = None,
                   layout: List[LayoutItem] = List(),
                   margin: List[Int] = List(),
                   rowHeight: Option[Int] = None,
                   isDraggable: Option[Boolean] = None,
                   isResizable: Option[Boolean] = None,
                   useCSSTransforms: Option[Boolean] = None,
                   preventCollision: Option[Boolean] = None,
                   onLayoutChange: Option[OnLayoutChange] = None,
                   onDragStart: Option[ItemCallback] = None,
                   onDrag: Option[ItemCallback] = None,
                   onDragStop: Option[ItemCallback] = None,
                   onResizeStart: Option[ItemCallback] = None,
                   onResize: Option[ItemCallback] = None,
                   onResizeStop: Option[ItemCallback] = None)

  override val component = ReactGridLayout
}

@JSImport("react-grid-layout", "ResponsiveGridLayout")
@js.native
object ReactResponsiveGridLayout extends js.Object

@react object ResponsiveGridLayout extends ExternalComponent {

  case class Props(width: Int,
                   layouts: ResponsiveLayout,
                   breakpoints: Option[Breakpoints] = None,
                   cols: Option[Cols] = None,
                   autoSize: Option[Boolean] = None,
                   draggableCancel: Option[String] = None,
                   draggableHandle: Option[String] = None,
                   verticalCancel: Option[Boolean] = None,
                   compactType: Option[String] = None,
                   layout: List[LayoutItem] = List(),
                   margin: List[Int] = List(),
                   rowHeight: Option[Int] = None,
                   isDraggable: Option[Boolean] = None,
                   isResizable: Option[Boolean] = None,
                   useCSSTransforms: Option[Boolean] = None,
                   preventCollision: Option[Boolean] = None,
                   onLayoutChange: Option[OnResponsiveLayoutChange] = None,
                   onDragStart: Option[ItemCallback] = None,
                   onDrag: Option[ItemCallback] = None,
                   onDragStop: Option[ItemCallback] = None,
                   onResizeStart: Option[ItemCallback] = None,
                   onResize: Option[ItemCallback] = None,
                   onResizeStop: Option[ItemCallback] = None,
                   onBreakpointChange: Option[OnBreakpointChange] = None,
                   onWidthChange: Option[OnWidthChange] = None)

  override val component = ReactResponsiveGridLayout
}

@JSImport("react-grid-layout", "GridItem")
@js.native
object ReactGridItem extends js.Object

@react object GridItem extends ExternalComponent {

  case class Props(i: String,
                   x: Int,
                   y: Int,
                   w: Int,
                   h: Int,
                   minW: Option[Int] = None,
                   maxW: Option[Int] = None,
                   minH: Option[Int] = None,
                   maxH: Option[Int] = None,
                   static: Option[Boolean] = None,
                   isDraggable: Option[Boolean] = None,
                   isResizable: Option[Boolean] = None)

  override val component = ReactGridItem
}

@JSImport("react-grid-layout", "WidthProvider")
@js.native
object ReactGridWidthProvider extends js.Object

@react object GridWidthProvider extends ExternalComponent {

  case class Props(measureBeforeMount: Boolean = true)

  override val component = ReactGridWidthProvider
}

case class LayoutItem(i: String,
                      x: Int,
                      y: Int,
                      w: Int,
                      h: Int,
                      minW: Option[Int],
                      maxW: Option[Int],
                      minH: Option[Int],
                      maxH: Option[Int],
                      static: Option[Boolean],
                      isDraggable: Option[Boolean],
                      isResizable: Option[Boolean])

case class Position(left: Int, top: Int, width: Int, height: Int)
case class Breakpoints(lg: Int, md: Int, sm: Int, xs: Int, xxs: Int)
case class ResponsiveLayout(lg: Int, md: Int, sm: Int, xs: Int, xxs: Int)
case class Cols(lg: Int, md: Int, sm: Int, xs: Int, xxs: Int)


object Types {
  type Layout = List[LayoutItem]
  type ItemCallback = (Layout, LayoutItem, LayoutItem, LayoutItem, MouseEvent, HTMLElement) => Unit
  type OnBreakpointChange = (String, Int) => Unit
  type OnLayoutChange = Layout => Unit
  type OnResponsiveLayoutChange = (Layout, ResponsiveLayout) => Unit
  type OnWidthChange = (Int, List[Int], Int, List[Int]) => Unit
}