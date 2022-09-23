package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.SingleColumnSelection
import com.harana.sdk.shared.models.flow.parameters._
import com.harana.sdk.shared.models.flow.utils.Id
import com.harana.sdk.shared.models.flow.{Action2To1Info, PortPosition}

import scala.reflect.runtime.{universe => ru}

trait JoinInfo extends Action2To1Info[DataFrameInfo, DataFrameInfo, DataFrameInfo]
  with Parameters
  with ActionDocumentation {

  import JoinInfo._

  val id: Id = "06374446-3138-4cf7-9682-f884990f3a60"
  val name = "Join"
  val since = Version(0, 4, 0)
  val category = SetAction

  override val inputPortsLayout = List(PortPosition.Left, PortPosition.Right)

  val joinTypeParameter = ChoiceParameter[JoinTypeChoice.Option]("join type")
  setDefault(joinTypeParameter, JoinTypeChoice.Inner())
  def getJoinType = $(joinTypeParameter)
  def setJoinType(value: JoinTypeChoice.Option): this.type = set(joinTypeParameter, value)

  val leftPrefixParameter = PrefixBasedColumnCreatorParameter("left prefix", emptyPrefixValidation = true)
  setDefault(leftPrefixParameter, "")
  def getLeftPrefix = $(leftPrefixParameter)
  def setLeftPrefix(value: String): this.type = set(leftPrefixParameter, value)

  val rightPrefixParameter = PrefixBasedColumnCreatorParameter("right prefix", emptyPrefixValidation = true)
  setDefault(rightPrefixParameter, "")
  def getRightPrefix = $(rightPrefixParameter)
  def setRightPrefix(value: String): this.type = set(rightPrefixParameter, value)

  val joinColumnsParameter = ParametersSequence[ColumnPair]("join columns")
  def getJoinColumns = $(joinColumnsParameter)
  def setJoinColumns(value: Seq[ColumnPair]): this.type = set(joinColumnsParameter, value)

  override val parameters = Array(joinTypeParameter, leftPrefixParameter, rightPrefixParameter, joinColumnsParameter)

  @transient
  lazy val portI_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  @transient
  lazy val portO_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  @transient
  lazy val portI_1: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  case class RenamedColumnNames(originalLeftColumns: Array[String], originalRightColumns: Array[String]) {

    val left = LeftColumnNames(originalLeftColumns.toIndexedSeq, getLeftPrefix)
    val right = RightColumnNames(originalRightColumns.toIndexedSeq, getRightPrefix, left.prefixed)

    abstract class ColumnNames(original: Seq[String], prefix: String) {
      val prefixed = original.map(col => prefix + col)
      val renamed: Seq[String]
      lazy val originalToRenamed: Map[String, String] = original.zip(renamed).toMap
      lazy val renamedToOriginal: Map[String, String] = renamed.zip(original).toMap
    }

    case class LeftColumnNames(original: Seq[String], prefix: String) extends ColumnNames(original, prefix) {
      val renamed = prefixed
    }

    case class RightColumnNames(original: Seq[String], prefix: String, leftPrefixed: Seq[String]) extends ColumnNames(original, prefix) {
      val renamed = prefixed
    }
  }
}

object JoinInfo extends JoinInfo {

  def apply() = new JoinInfo {}

  case class ColumnPair() extends Parameters {

    val leftColumnParameter = SingleColumnSelectorParameter("left column", portIndex = 0)
    def getLeftColumn = $(leftColumnParameter)
    def setLeftColumn(value: SingleColumnSelection): this.type = set(leftColumnParameter, value)

    val rightColumnParameter = SingleColumnSelectorParameter("right column", portIndex = 1)
    def getRightColumn = $(rightColumnParameter)
    def setRightColumn(value: SingleColumnSelection): this.type = set(rightColumnParameter, value)

    val parameters = Array(leftColumnParameter, rightColumnParameter)

  }
}

object JoinTypeChoice {

  sealed abstract class Option(val name: String) extends Choice {
    val toSpark: String
    val choiceOrder: List[ChoiceOption] = List(classOf[Inner], classOf[Outer], classOf[LeftOuter], classOf[RightOuter])
    val parameters = Array.empty[Parameter[_]]
  }

  case class Inner() extends Option("Inner") { val toSpark = "inner" }
  case class Outer() extends Option("Outer") { val toSpark = "outer" }
  case class LeftOuter() extends Option("Left outer") { val toSpark = "left_outer" }
  case class RightOuter() extends Option("Right outer") { val toSpark = "right_outer" }

}