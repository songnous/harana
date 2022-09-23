package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.{Action1To2Info, PortPosition, parameters}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasSeedParameter
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
  val since = Version(0, 4, 0)
  val category = SetAction

  override def outputPortsLayout = List(PortPosition.Left, PortPosition.Right)

  val splitModeParameter = ChoiceParameter[SplitModeChoice]("split mode")
  setDefault(splitModeParameter, SplitModeChoice.Random())
  def getSplitMode = $(splitModeParameter)
  def setSplitMode(value: SplitModeChoice): this.type = set(splitModeParameter, value)

  override val parameters =  Left(Array(splitModeParameter))

  @transient
  lazy val portI_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  @transient
  lazy val portO_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  @transient
  lazy val portO_1: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

}

object SplitInfo extends SplitInfo {
  def apply(pos: (Int, Int), color: Option[String] = None) = new SplitInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}

sealed trait SplitModeChoice extends Choice {
  import SplitModeChoice._
  val choiceOrder: List[ChoiceOption] = List(classOf[Random], classOf[Conditional])
}

object SplitModeChoice {

  case class Random() extends SplitModeChoice with HasSeedParameter {
    val name = "RANDOM"

    val splitRatioParameter = DoubleParameter("split ratio", validator = RangeValidator(0.0, 1.0))
    setDefault(splitRatioParameter, 0.5)
    def getSplitRatio = $(splitRatioParameter)
    def setSplitRatio(value: Double): this.type = set(splitRatioParameter, value)

    def getSeed = $(seedParameter)
    def setSeed(value: Long): this.type = set(seedParameter, value)

    val parameters = Left(Array(splitRatioParameter, seedParameter))

  }

  case class Conditional() extends SplitModeChoice {
    val name = "CONDITIONAL"

    val conditionParameter = CodeSnippetParameter("condition", language = CodeSnippetLanguage(CodeSnippetLanguage.sql))
    def getCondition = $(conditionParameter)
    def setCondition(value: String): this.type = set(conditionParameter, value)

    val parameters = Left(Array(conditionParameter))
  }
}
