package com.harana.ui

import com.harana.designer.frontend.Circuit
import com.harana.ui.components.elements.Dialog
import com.harana.ui.components.elements.DialogStyle
import enumeratum.values.{IntCirceEnum, IntEnum, IntEnumEntry, StringCirceEnum, StringEnum, StringEnumEntry}
import enumeratum.{CirceEnum, Enum, EnumEntry}
import slinky.core.facade.{ReactElement, ReactRef}
import slinky.core.{BuildingComponent, KeyAndRefAddingStage, StatelessComponent, WithAttrs}
import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import diode.{Action => DiodeAction}
import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup, ParameterValue}
import com.harana.sdk.shared.models.common.Parameter.{ParameterName, ParameterValues}
import org.scalajs.dom.window
import com.harana.designer.frontend.Router
import com.harana.designer.frontend.navigation.NavigationStore.OpenRoute

import scala.scalajs.js
import scala.scalajs.reflect.Reflect

package object components {

  @inline implicit def convertWithAttrsOption(withAttrs: Option[WithAttrs[_]]): Option[ReactElement] =
    withAttrs.map(WithAttrs.build)

  @inline implicit def convertWithAttrsList(withAttrs: List[WithAttrs[_]]): List[ReactElement] =
    withAttrs.map(WithAttrs.build)

  @inline implicit def convertBuildingComponentOption[E, R <: js.Object](comp: Option[BuildingComponent[E, R]]): Option[ReactElement] =
    comp.map(BuildingComponent.make)

  @inline implicit def convertBuildingComponentList[E, R <: js.Object](comp: List[BuildingComponent[E, R]]): List[ReactElement] =
    comp.map(BuildingComponent.make)

  @inline implicit def convertKeyAndRefOption[D](stage: Option[KeyAndRefAddingStage[D]]): Option[ReactElement] =
    stage.map(KeyAndRefAddingStage.build)

  @inline implicit def convertKeyAndRefList[D](stage: List[KeyAndRefAddingStage[D]]): List[ReactElement] =
    stage.map(KeyAndRefAddingStage.build)

  @inline def literal(tuples: (String, js.Any)*) = {
    val literal = js.Dynamic.literal()
    tuples.foreach { case (k, v) =>
      literal.updateDynamic(k)(v)
    }
    literal
  }

  @inline def optEnum(option: Option[StringEnumEntry]): String =
    option.map(_.value).getOrElse("")

  @inline def when(condition: Boolean)(component: => ReactElement): ReactElement =
    if (condition) component else null

  @inline def when(condition: => Boolean, component: => ReactElement): ReactElement =
    if (condition) component else null

  @inline def when(condition: Option[_], component: => ReactElement): ReactElement =
    if (condition.isDefined) component else null

  @inline def whenNot(condition: Option[_], component: => ReactElement): ReactElement =
    if (condition.isEmpty) component else null

  @inline def when[A >: StatelessComponent](condition: Option[A]): ReactElement =
    condition.map(_.asInstanceOf[StatelessComponent].render()).getOrElse(null.asInstanceOf[ReactElement])

  @inline def whenRef[A <: js.Any](condition: Option[Ref[A]]) =
    if (condition.isDefined) KeyAndRefAddingStage.build(condition.get) else null.asInstanceOf[ReactElement]

  @inline def whenNotRef[A <: js.Any](condition: Option[Ref[A]]) =
    if (condition.isEmpty) KeyAndRefAddingStage.build(condition.get) else null.asInstanceOf[ReactElement]

  @inline def cssSet(ps: (String, Boolean)*): String =
    ps.filter(_._2).map(_._1).mkString(" ")

  @inline def htmlColumnWidth(width: Int) = s"col-md-%width"

  @inline def getComponent(model: Any) = {
    val classNameName = model.getClass.getName.replaceAll("sdk", "ui") + "Component$"
    Reflect.lookupLoadableModuleClass(classNameName).map { className =>
      //			className.loadModule().asInstanceOf[Component].apply(Props(model))
    }.orElse {
      println(s"Failed to load parameter: $classNameName")
      None
    }
  }

  @inline def openLink(linkType: LinkType) =
    linkType match {
      case LinkType.Action(action) =>
        Circuit.dispatch(action)

      case LinkType.HideDialog(ref) =>
        ref.current.hide()

      case LinkType.Page(name) =>
        Circuit.dispatch(OpenRoute(name))

      case LinkType.OnClick(click) =>
        click()

      case LinkType.ShowDialog(ref, title, style, initialValues, width) =>
        ref.current.show(title, style, initialValues, width)

      case LinkType.Url(url) =>
        window.location.href = url
    }

  @inline val emptyElement = null.asInstanceOf[ReactElement]

  def emptyFn = () => {}
  def emptyFn[T] = (s: T) => {}

  sealed abstract class Border(val value: String) extends StringEnumEntry
  case object Border extends StringEnum[Border] with StringCirceEnum[Border] {
    case object Left extends Border("left")
    case object Right extends Border("right")
    case object Top extends Border("top")
    case object Bottom extends Border("bottom")
    val values = findValues
  }

  sealed abstract class BorderSize(val value: String) extends StringEnumEntry
  case object BorderSize extends StringEnum[BorderSize] with StringCirceEnum[BorderSize] {
    case object Basic extends BorderSize("sm")
    case object Large extends BorderSize("lg")
    case object ExtraLarge extends BorderSize("xl")
    val values = findValues
  }

  sealed abstract class Device(val value: String) extends StringEnumEntry
  case object Device extends StringEnum[Device] with StringCirceEnum[Device] {
    case object Phone extends Device("xs")
    case object Tablet extends Device("sm")
    case object Laptop extends Device("md")
    case object Desktop extends Device("lg")
    val values = findValues
  }

  sealed abstract class ColumnSize(val value: Int) extends IntEnumEntry
  case object ColumnSize extends IntEnum[ColumnSize] with IntCirceEnum[ColumnSize] {
    case object One extends ColumnSize(1)
    case object Two extends ColumnSize(2)
    case object Three extends ColumnSize(3)
    case object Four extends ColumnSize(4)
    case object Five extends ColumnSize(5)
    case object Six extends ColumnSize(6)
    case object Twelve extends ColumnSize(12)
    val values = findValues
  }

  sealed abstract class HorizontalPosition(val value: String) extends StringEnumEntry
  case object HorizontalPosition extends StringEnum[HorizontalPosition] with StringCirceEnum[HorizontalPosition] {
    case object Left extends HorizontalPosition("left")
    case object Right extends HorizontalPosition("right")
    val values = findValues
  }

  sealed trait LinkType
  object LinkType {
    case class Action(action: DiodeAction) extends LinkType
    case class HideDialog(ref: ReactRef[Dialog.Def]) extends LinkType
    case class Menu(menu: com.harana.ui.external.shoelace.Menu.Props) extends LinkType
    case class OnClick(onClick: () => Unit) extends LinkType
    case class Page(name: String) extends LinkType
    case class ShowDialog(ref: ReactRef[Dialog.Def], 
                          style: DialogStyle,
                          initialValues: Option[Map[ParameterName, ParameterValue]] = None,
                          title: Option[String],
                          width: Option[String] = scala.None) extends LinkType
    case class Url(url: String) extends LinkType
  }

  sealed trait Orientation extends EnumEntry
  case object Orientation extends Enum[Orientation] with CirceEnum[Orientation] {
    case object Horizontal extends Orientation
    case object Vertical extends Orientation
    val values = findValues
  }

  sealed trait ParameterGroupLayout
  object ParameterGroupLayout {
    case object List extends ParameterGroupLayout
    case class Grid(columnSize: ColumnSize) extends ParameterGroupLayout
  }

  case class Percentage(percentage: Double, name: String)

  sealed abstract class Position(val value: String) extends StringEnumEntry
  case object Position extends StringEnum[Position] with StringCirceEnum[Position] {
    case object Left extends Position("left")
    case object Right extends Position("right")
    case object Top extends Position("top")
    case object Bottom extends Position("bottom")
    val values = findValues
  }

  case class RGBA(r: Int, g: Int, b: Int, a: Float)

  sealed abstract class Size(val value: String) extends StringEnumEntry
  case object Size extends StringEnum[Size] with StringCirceEnum[Size] {
    case object ExtraLarge extends Size("xl")
    case object Large extends Size("lg")
    case object Small extends Size("sm")
    case object Mini extends Size("xs")
    val values = findValues
  }

  case class Value(value: Double, name: String)

  type Ref[T <: js.Any] = KeyAndRefAddingStage[T]
  type Url = String
}
