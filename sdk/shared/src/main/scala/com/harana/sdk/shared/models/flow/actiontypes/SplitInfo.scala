package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasSeedParameter
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters._
import com.harana.sdk.shared.models.flow.utils.Id
import com.harana.sdk.shared.models.flow.{Action1To2TypeInfo, PortPosition}
import izumi.reflect.Tag

import scala.reflect.runtime.{universe => ru}

trait SplitInfo extends Action1To2TypeInfo[DataFrameInfo, DataFrameInfo, DataFrameInfo]
  with Parameters
  with ActionDocumentation {

  val id: Id = "d273c42f-b840-4402-ba6b-18282cc68de3"
  val name = "split"
  val since = Version(0, 4, 0)
  val category = SetAction

  override def outputPortsLayout = List(PortPosition.Left, PortPosition.Right)

  val splitModeParameter = ChoiceParameter[SplitModeChoice]("split-mode", default = Some(SplitModeChoice.Random()))
  def getSplitMode = $(splitModeParameter)
  def setSplitMode(value: SplitModeChoice): this.type = set(splitModeParameter, value)

  override val parameterGroups = List(ParameterGroup("", splitModeParameter))

  @transient
  lazy val portI_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

  @transient
  lazy val portO_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

  @transient
  lazy val portO_1: Tag[DataFrameInfo] = Tag[DataFrameInfo]

}

object SplitInfo extends SplitInfo

sealed trait SplitModeChoice extends Choice {
  import SplitModeChoice._
  val choiceOrder: List[ChoiceOption] = List(classOf[Random], classOf[Conditional])
}

object SplitModeChoice {

  case class Random() extends SplitModeChoice with HasSeedParameter {
    val name = "random"

    val splitRatioParameter = DoubleParameter("split-ratio", default = Some(0.5), validator = RangeValidator(0.0, 1.0))
    def getSplitRatio = $(splitRatioParameter)
    def setSplitRatio(value: Double): this.type = set(splitRatioParameter, value)

    def getSeed = $(seedParameter)
    def setSeed(value: Long): this.type = set(seedParameter, value)

    override val parameterGroups = List(ParameterGroup("", splitRatioParameter, seedParameter))

  }

  case class Conditional() extends SplitModeChoice {
    val name = "conditional"

    val conditionParameter = CodeSnippetParameter("condition", language = CodeSnippetLanguage.SQL)
    def getCondition = $(conditionParameter)
    def setCondition(value: String): this.type = set(conditionParameter, value)

    override val parameterGroups = List(ParameterGroup("", conditionParameter))
  }
}
