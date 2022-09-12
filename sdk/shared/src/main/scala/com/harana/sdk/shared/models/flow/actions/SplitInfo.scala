package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.{Action1To2Info, PortPosition, parameters}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasSeedParameter
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter, DoubleParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.{universe => ru}

trait SplitInfo extends Action1To2Info[DataFrameInfo, DataFrameInfo, DataFrameInfo]
  with Parameters
  with ActionDocumentation {

  val id: Id = "d273c42f-b840-4402-ba6b-18282cc68de3"
  val name = "Split"
  val description = "Splits a DataFrame into two DataFrames"
  val since = Version(0, 4, 0)
  val category = SetAction

  override def outPortsLayout = Vector(PortPosition.Left, PortPosition.Right)

  val splitModeParameter = ChoiceParameter[SplitModeChoice]("split mode",
    description = Some("""There are two split modes:
                         |`RANDOM` where rows are split randomly with specified `ratio` and `seed`;
                         |`CONDITIONAL` where rows are split into two `DataFrames` -
                         |satisfying an SQL `condition` and not satisfying it.
                         |""".stripMargin)
  )

  setDefault(splitModeParameter, SplitModeChoice.Random())
  def getSplitMode = $(splitModeParameter)
  def setSplitMode(value: SplitModeChoice): this.type = set(splitModeParameter, value)

  override val parameters =  Array(splitModeParameter)

  @transient
  lazy val portI_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  @transient
  lazy val portO_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  @transient
  lazy val portO_1: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

}

object SplitInfo extends SplitInfo

sealed trait SplitModeChoice extends Choice {
  import SplitModeChoice._
  val choiceOrder: List[ChoiceOption] = List(classOf[Random], classOf[Conditional])
}

object SplitModeChoice {

  case class Random() extends SplitModeChoice with HasSeedParameter {
    val name = "RANDOM"

    val splitRatioParameter = DoubleParameter("split ratio", Some("Percentage of rows that should end up in the first output DataFrame."), validator = RangeValidator(0.0, 1.0))
    setDefault(splitRatioParameter, 0.5)
    def getSplitRatio = $(splitRatioParameter)
    def setSplitRatio(value: Double): this.type = set(splitRatioParameter, value)

    def getSeed = $(seedParameter)
    def setSeed(value: Long): this.type = set(seedParameter, value)

    val parameters = Array(splitRatioParameter, seedParameter)

  }

  case class Conditional() extends SplitModeChoice {
    val name = "CONDITIONAL"

    val conditionParameter = CodeSnippetParameter("condition",
      description = Some("""Condition used to split rows.
                           |Rows that satisfy condition will be placed in the first DataFrame
                           |and rows that do not satisfy it - in the second.
                           |Use SQL syntax.""".stripMargin),
      language = CodeSnippetLanguage(CodeSnippetLanguage.sql)
    )

    def getCondition = $(conditionParameter)
    def setCondition(value: String): this.type = set(conditionParameter, value)

    val parameters = Array(conditionParameter)
  }
}
