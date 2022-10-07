package com.harana.designer.frontend.flows.item.ui

import java.util.concurrent.atomic.AtomicReference
import com.harana.designer.frontend.flows.item.FlowItemStore._
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.ui.components.when
import com.harana.ui.components.LinkType
import com.harana.ui.components.elements.{Dialog, DialogStyle, HeadingItem, Page}
import com.harana.ui.components.sidebar.{ContentSection, Sidebar, SidebarSection, Tab}
import com.harana.ui.external.flow._
import com.harana.ui.external.flow.types.{FlowElement, FlowNode}
import com.harana.ui.external.shoelace.{Menu, MenuItem, MenuLabel}
import diode.ActionBatch
import diode.AnyAction._
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks.useEffect
import slinky.core.facade.{Fragment, React}
import slinky.web.html._
import typings.std.MouseEvent
import com.harana.designer.frontend.flows.item.ui.sidebar._
import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.utils.HMap
import com.harana.ui.external.lazy_log.LazyLog

import scala.scalajs.js.JSConverters._
import scala.scalajs.js

@react object FlowItemPage {

  private val currentFlow = new AtomicReference[String]("")
  val dialogRef = React.createRef[Dialog.Def]

  val component = FunctionalComponent[js.Dynamic] { props =>
    val state = Circuit.state(zoomTo(_.flowItemState))

    useEffect(() => {
      val routeId = props.selectDynamic("match").params.selectDynamic("id").toString
      if (!currentFlow.get.equals(routeId)) {
        Circuit.dispatch(OpenFlow(routeId))
        currentFlow.set(routeId)
      }
    })

    Fragment(
      Dialog.withRef(dialogRef),
      Page(
        title = i"heading.section.flows",
        subtitle = state.flow.map(_.title),
        navigationBar = Some(Navigation(())),
        toolbarItems = headingItems(state),
        content = pageContent(state),
        fixedSizeContent = true
      )
    )
  }


  def pageContent(state: FlowItemState) = {
    val tabs = sidebarTabs(state)
    val selectedTab = tabs.find(_.name.equals(state.selectedTab.toString))

    div(className := "flow-container")(
      div(className := "flow-sidebar")(
        Sidebar(List(), sidebarTabs(state), selectedTab, Some((tab: Tab) => Circuit.dispatch(SelectTab(FlowTab.withName(tab.name)))), showBorder = false, fixed = true, padding = false, separateCategories = false)
      ),
      div(className := "flow-grid", onDrop := (e => Circuit.dispatch(AddAction(e))), onDragOver := (e => e.preventDefault()))(
        if (state.showLogs)
          div(className := "newflow-logs")(
            when(state.flow, LazyLog(url = s"/api/execution/flows/logs/${state.flow.get.id}", lineClassName = "newflow-logs-line", enableSearch = true, caseInsensitive = true, stream = true).withKey(state.logsKey))
          )
        else
          Flow(
            elements = state.nodes ++ state.edges,
            nodeTypes = new NodeTypes { val actionNode = ActionNode.component },
            nodesConnectable = !state.isRunning,
            snapToGrid = state.snapToGrid,
            onConnect = (connection: Connection) => Circuit.dispatch(AddConnection(connection)),
            onLoad = (instance: FlowInstance) => Circuit.dispatch(UpdateFlowInstance(instance)),
            onNodeDragStop = (_: MouseEvent, node: FlowNode) => Circuit.dispatch(UpdateNode(node)),
            onElementClick = (_: MouseEvent, element: FlowElement) =>
              if (isNode(element)) {
                val flowNode = element.asInstanceOf[FlowNode]
                Circuit.dispatch(SelectAction(toNode(flowNode).id))
              },
            onElementsRemove = (elements: js.Array[FlowElement]) =>
              if (!state.isEditingParameters && !state.isRunning)
                Circuit.dispatch(DeleteElements(elements.toList)),
            onPaneClick = (_: MouseEvent) => Circuit.dispatch(DeselectAllActions)
          )( 
            when(state.showGrid)(Background()),
            when(state.showMiniMap)(MiniMap(nodeColor = "#eeeeee", maskColor = "#00000000", className = "newflow-minimap"))
          )
      )
    )
  }


  def headingItems(state: FlowItemState) =
    List(
      HeadingItem.IconButton(("icomoon", "undo2"), i"flows.menu.zoom-in", LinkType.Action(Undo), enabled = state.undoHistory.canUndo),
      HeadingItem.IconButton(("icomoon", "redo2"), i"flows.menu.zoom-in", LinkType.Action(Redo), enabled = state.undoHistory.canRedo),

      HeadingItem.IconButton(("icomoon", "zoom-in"), i"flows.menu.zoom-in", LinkType.Action(ZoomIn)),
      HeadingItem.IconButton(("icomoon", "zoom-out"), i"flows.menu.zoom-out", LinkType.Action(ZoomOut)),
      HeadingItem.IconButton(("icomoon", "terminal"), i"flows.menu.logs", LinkType.Action(ToggleLogs)),

      if (state.isRunning)
        HeadingItem.IconButton(("icomoon", "stop2"), i"flows.menu.stop-flow", LinkType.OnClick(() => 
          dialogRef.current.show(
            title = Some(i"flows.menu.stop-flow"),
            style = DialogStyle.Confirm(i"flows.menu.stop-flow.confirmation", i"flows.menu.stop-flow.stop", onOk = Some(() => Circuit.dispatch(StopFlow)))
          )
        ))
      else
        HeadingItem.IconButton(("icomoon", "play4"), i"flows.menu.start-flow", LinkType.Action(StartFlow)),

      HeadingItem.IconMenu(
        icon = ("icomoon", "grid5  "), i"flows.menu.options",
        className = Some("heading-icon"),
        menuItems = List(
          MenuLabel(i"flows.menu.options.grid"),
          MenuItem(i"flows.menu.options.grid.show-grid", iconPrefix = if (state.showGrid) Some("icomoon", "checkmark3") else None, onClick = Some(_ => Circuit.dispatch(ToggleShowGrid))),
          MenuItem(i"flows.menu.options.grid.show-minimap", iconPrefix = if (state.showMiniMap) Some("icomoon", "checkmark3") else None, onClick = Some(_ => Circuit.dispatch(ToggleShowMiniMap))),
          MenuItem(i"flows.menu.options.grid.snap-to-grid", iconPrefix = if (state.snapToGrid) Some("icomoon", "checkmark3") else None, onClick = Some(_ => Circuit.dispatch(ToggleSnapToGrid))),
        )
      )
    )


  def sidebarTabs(state: FlowItemState) = {
    val actionTypesCategory = SidebarSection(None, allowCollapse = false, allowClear = false, None, ContentSection(actions(state), padding = false))
    val dataSourcesCategory = SidebarSection(None, allowCollapse = false, allowClear = false, None, ContentSection(actions(state), padding = false))

    val result = for {
      actionId          <- state.selectedActionId
      node              <- state.nodes.find(_.id == actionId.toString)
      result            =  (node.data.actionType, node.data.parameterValues)
    } yield result

    val parametersCategory = SidebarSection(
      None,
      allowCollapse = false,
      allowClear = false,
      None,
      parameters(state.flow, result.map(_._1), state.selectedActionId, result.map(_._2).getOrElse(HMap.empty[Parameter.Values]), state.isRunning)
    )

    val runCategory = SidebarSection(
      None,
      allowCollapse = false,
      allowClear = false,
      None,
      ContentSection(div(
        runStatus(state.flowExecution),
        runTime(state.flowExecution),
        runHealth(state.flowExecution),
        runResources(state.flowExecution),
        runShuffle(state.flowExecution))
      ))

    List(
      Tab(FlowTab.ActionTypes.toString, List(actionTypesCategory), Some("icomoon", "cube4")),
      Tab(FlowTab.Parameters.toString, List(parametersCategory), Some("icomoon", "equalizer2")),
      Tab(FlowTab.Run.toString, List(runCategory), Some("icomoon", "chart"))
    )
  }
}