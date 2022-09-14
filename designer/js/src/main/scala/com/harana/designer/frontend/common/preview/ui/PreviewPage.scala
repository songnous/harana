package com.harana.designer.frontend.common.preview.ui

import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.designer.frontend.utils.{FileUtils, i18nUtils}
import com.harana.designer.shared.PreviewData
import com.harana.shared.models.HaranaFile
import com.harana.ui.components.Ref
import com.harana.ui.components.elements.{HeadingItem, NavigationBar, Page}
import com.harana.ui.components.sidebar._
import com.harana.ui.external.axui_data_grid.Column
import com.harana.ui.external.shoelace.Menu
import com.harana.ui.external.syntax_highlighter.{HighlightStyle, SyntaxHighlighter}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._


@react class PreviewPage extends StatelessComponent {

  case class Props(file: HaranaFile,
                   path: List[String],
                   title: String,
                   subtitle: Option[String] = None,
                   subtitleMenu: Option[Menu.Props] = None,
                   preview: Option[Either[String, PreviewData]] = None,
                   navigationBar: Option[ReactElement] = None,
                   fixedNavigationBar: Boolean = true,
                   footerNavigationBar: Option[ReactElement] = None,
                   toolbarItems: List[HeadingItem] = List(),
                   blocked: Boolean = false,
                   sidebarAboutItems: List[TextListItem])


	override def shouldComponentUpdate(nextProps: Props, nextState: Unit) =
    !(props.path.equals(nextProps.path)) || !(props.file.equals(nextProps.file))
  
  
  def sidebar =
    Sidebar(
      List(SidebarSection(Some(i"files.sidebar.about"), allowCollapse = false, allowClear = false, None, TextListSection(props.sidebarAboutItems)))
    )


  def codePreview = 
    SyntaxHighlighter(language = FileUtils.highlightType(props.file), HighlightStyle.atomOneLight)(props.preview.map(_.left.get))


  def imagePreview = {
    img(src := s"/api/files/preview?path=${props.path.mkString("/")}")
  }


  def tablePreview: ReactElement = {
    if (props.preview.isDefined) {
      val preview = props.preview.get.toOption.get
      val columns = preview.headers.map { header => new Column {
        override val key = header
        override val label = header
      }}

      div(className := "panel panel-flat")(
        div(className := "table-responsive")(
          table(className := "table text-nowrap preview-table")(
            preview.headers.map(th(_)),
            preview.rows.map(r =>
              tr(r.map(td(_)))
            )
          )
        )
      )

    }else
      div()
  }


  def unknownPreview =
    div(className := "panel panel-flat")(
      div(className := "panel-body panel-fullscreen panel-centered")(
        "Preview not available for this file type"
      )
    )


  def render() =
    Page(
      title = props.title,
      subtitle = props.subtitle,
      subtitleMenu = props.subtitleMenu,
      navigationBar = props.navigationBar,
      fixedNavigationBar = props.fixedNavigationBar,
      footerNavigationBar = props.footerNavigationBar,
      toolbarItems = props.toolbarItems,
      blocked = props.blocked,
      sidebar = Some(sidebar),
      content =
        props.file match {
          case f if FileUtils.isCode(f) => codePreview
          case f if FileUtils.isImage(f) => imagePreview
          case f if FileUtils.isTabular(f) => tablePreview
          case _ => unknownPreview
        }
    )
}