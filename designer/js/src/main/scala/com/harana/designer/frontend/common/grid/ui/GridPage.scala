package com.harana.designer.frontend.common.grid.ui

import com.harana.designer.frontend.common.grid.GridStore._
import com.harana.designer.frontend.common.ui.{ViewMode, filterSection, searchSection}
import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.designer.frontend.{Circuit, State => AppState}
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup
import com.harana.sdk.shared.utils.Random
import com.harana.ui.components._
import com.harana.ui.components.elements._
import com.harana.ui.components.sidebar._
import com.harana.ui.components.structure.Grid
import com.harana.ui.components.table.{Column, GroupedTable, Row, RowGroup}
import com.harana.ui.components.widgets.PillWidget
import com.harana.ui.external.shoelace.Radio
import diode.{ActionResult, Dispatcher}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, React, ReactElement}

import scala.collection.mutable.ListBuffer

@react class GridPage extends StatelessComponent {

  case class Props(itemType: String,
                   state: GridState[_, _],
                   title: String,
                   tableColumns: List[Column],
                   tableContent: (Column, GridPageItem) => ReactElement,
                   toolbarItems: List[HeadingItem] = List(),
                   itemSubTypes: List[(String, EntitySubType)] = List(),
                   itemMenuItems: Option[GridPageItem => List[ReactElement]] = None,
                   fixedNavigationBar: Boolean = true,
                   footerNavigationBar: Option[ReactElement] = None,
                   showOwners: Boolean = true,
                   showTags: Boolean = true,
                   allowDelete: Boolean = true,
                   allowEdit: Boolean = true,
                   editParameterGroupLayout: Option[ParameterGroup => ParameterGroupLayout] = None,
                   editWidth: Option[String] = None,
                   editAdditionalSections: List[(String, ReactElement)] = List())

  val drawerRef = React.createRef[Drawer.Def]
  val deleteDialogRef = React.createRef[Dialog.Def]

  override def componentDidMount() = {
    Circuit.addProcessor((_: Dispatcher, action: Any, next: Any => ActionResult[AppState], _: AppState) => {
      action match {
        case ShowEditDialog(entityType, title) =>
          if (entityType == props.itemType)
            drawerRef.current.show(
              style = editStyle,
              title = title,
              values = Some(props.state.editValues),
              width = props.editWidth
            )

        case RefreshEditDialog(entityType, values) =>
          if (entityType == props.itemType)
            drawerRef.current.update(
              style = editStyle,
              values = values
            )
        case _ =>
      }
      next(action)
    })
  }


  def editStyle = {
    DrawerStyle.Sectioned(
      parametersOrSections = Left(
        DrawerParameters(
          groups = props.state.editParameterGroups,
          i18nPrefix = props.itemType,
          layout = props.editParameterGroupLayout,
          additionalSections = props.editAdditionalSections
        )
      ),
      onChange = Some((parameter, value) =>
        Circuit.dispatch(OnEditParameterChange(props.itemType, parameter, value))
      ),
      onOk = Some(values => {
        val item = props.state.selectedItem
        Circuit.dispatch(if (item.isEmpty) SaveNewItem(props.itemType, values) else SaveExistingItem(props.itemType, item.get.id, values))
      })
    )
  }


  def headingItems = {
    val headingMenu = if (props.state.viewMode == ViewMode.List)
      List(menus.headingMenu(props.state.viewMode, props.itemType, props.state.selectedItem, props.allowEdit, props.allowDelete, deleteDialogRef, props.itemMenuItems))
    else
      List()

    headingMenu ++ menus.newItem(props.itemType, props.itemSubTypes, props.allowEdit) ++
      List(menus.viewMode(props.itemType, props.state.viewMode), menus.sort(props.itemType, props.state.sortOrdering))
  }


  def sidebar = {
    val sections = new ListBuffer[SidebarSection]()
    sections += searchSection(props.state.searchQuery, query => Circuit.dispatch(UpdateSearchQuery(props.itemType, query)))
    if (props.showOwners && props.state.owners.nonEmpty) sections += filterSection("Owner", props.state.owners, props.state.owner, owner => Circuit.dispatch(UpdateOwner(props.itemType, owner)))
    if (props.showTags && props.state.tags.nonEmpty) sections += filterSection(i"common.sidebar.tags", props.state.tags, props.state.tag, tag => Circuit.dispatch(UpdateTag(props.itemType, tag)))

    Sidebar(sections.toList)
  }


  def content =
    props.state.viewMode match {
      case ViewMode.List =>
        val rows = props.state.items.map { item =>
          val checked = props.state.selectedItem.isDefined && props.state.selectedItem.get == item
          val radio = Some(Radio.Props(name = "s", checked = Some(checked), onChange = Some(checked => if (checked) Circuit.dispatch(UpdateSelectedItem(props.itemType, Some(item))))))
          Row(props.tableColumns.map(column => column -> props.tableContent(column, item)).toMap, radio, None, onDoubleClick = Some(() => openLink(item.link)))
        }
        GroupedTable(props.tableColumns, List(RowGroup(None, rows))).withKey(Random.short)

      case ViewMode.Grid =>
        Grid(
          props.state.items.map { item =>
            PillWidget(
              title = item.title,
              subtitle = item.description,
              chartType = None,
              link = item.link,
              background = item.background.get,
              menuItems = menus.itemMenu(props.state.viewMode, props.itemType, item, props.allowEdit, props.allowDelete, deleteDialogRef, props.itemMenuItems)
            ).withKey(item.id)
          },
          ColumnSize.Three
        )
    }


  def render() = {
    Fragment(
      Drawer().withRef(drawerRef),
      Dialog().withRef(deleteDialogRef),
      Page(
        title = props.title,
        subtitle = None,
        navigationBar = Some(Navigation(())),
        fixedNavigationBar = props.fixedNavigationBar,
        footerNavigationBar = props.footerNavigationBar,
        toolbarItems = props.toolbarItems ++ headingItems,
        blocked = props.state.blocked,
        sidebar = Some(sidebar),
        content = Some(content)
      )
    )
  }
}