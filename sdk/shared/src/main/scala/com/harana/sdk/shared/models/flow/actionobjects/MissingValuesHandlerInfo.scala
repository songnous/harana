package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters._
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.MultipleColumnSelection
import com.harana.sdk.shared.models.flow.parameters.validators.RegexValidator

trait MissingValuesHandlerInfo extends TransformerInfo {

  import MissingValuesHandlerInfo._

  val id = "A067DB08-3135-4CA7-A49D-BCAA0263C906"

  val selectedColumnsParameter = ColumnSelectorParameter("columns", portIndex = 0)
  def getSelectedColumns = $(selectedColumnsParameter)
  def setSelectedColumns(value: MultipleColumnSelection): this.type = set(selectedColumnsParameter, value)

  val strategyParameter = ChoiceParameter[Strategy]("strategy")
  setDefault(strategyParameter, Strategy.RemoveRow())
  def getStrategy = $(strategyParameter)
  def setStrategy(value: Strategy): this.type = set(strategyParameter, value)

  val userDefinedMissingValuesParameter = ParametersSequence[UserDefinedMissingValue]("user-defined missing values")
  def getUserDefinedMissingValues = $(userDefinedMissingValuesParameter).map(_.getMissingValue)
  def setUserDefinedMissingValues(value: Seq[String]): this.type = set(userDefinedMissingValuesParameter, value.map(UserDefinedMissingValue().setMissingValue(_)))
  setDefault(userDefinedMissingValuesParameter, Seq(UserDefinedMissingValue().setMissingValue("NA"), UserDefinedMissingValue().setMissingValue("NaN")))

  val missingValueIndicatorParameter = ChoiceParameter[MissingValueIndicatorChoice]("missing value indicator")
  setDefault(missingValueIndicatorParameter, MissingValueIndicatorChoice.No())
  def getMissingValueIndicator = $(missingValueIndicatorParameter)
  def setMissingValueIndicator(value: MissingValueIndicatorChoice): this.type = set(missingValueIndicatorParameter, value)

  val parameters = Left(Array(
    selectedColumnsParameter,
    strategyParameter,
    missingValueIndicatorParameter,
    userDefinedMissingValuesParameter
  ))
}

object MissingValuesHandlerInfo extends MissingValuesHandlerInfo {

  sealed trait Strategy extends Choice {
    import MissingValuesHandlerInfo.Strategy._
    val choiceOrder: List[ChoiceOption] = List(classOf[RemoveRow], classOf[RemoveColumn], classOf[ReplaceWithCustomValue], classOf[ReplaceWithMode])
  }

  object Strategy {

    case class RemoveRow() extends Strategy {
      val name = "remove row"
      val parameters = Left(Array.empty[Parameter[_]])
    }

    case class RemoveColumn() extends Strategy {
      val name = "remove column"
      val parameters = Left(Array.empty[Parameter[_]])
    }

    case class ReplaceWithCustomValue() extends Strategy {
      val name = "replace with custom value"
      val customValueParameter = StringParameter("value")
      def getCustomValue = $(customValueParameter)
      def setCustomValue(value: String): this.type = set(customValueParameter, value)

      val parameters = Left(Array(customValueParameter))
    }

    case class ReplaceWithMode() extends Strategy {
      val name = "replace with mode"
      val emptyColumnStrategyParameter = ChoiceParameter[EmptyColumnsStrategy]("empty column strategy")
      setDefault(emptyColumnStrategyParameter, EmptyColumnsStrategy.RemoveEmptyColumns())
      def getEmptyColumnStrategy = $(emptyColumnStrategyParameter)
      def setEmptyColumnStrategy(value: EmptyColumnsStrategy): this.type = set(emptyColumnStrategyParameter, value)

      val parameters = Left(Array(emptyColumnStrategyParameter))
    }
  }

  sealed trait EmptyColumnsStrategy extends Choice {
    import MissingValuesHandlerInfo.EmptyColumnsStrategy._
    val choiceOrder: List[ChoiceOption] = List(classOf[RemoveEmptyColumns], classOf[RetainEmptyColumns])
  }

  object EmptyColumnsStrategy {

    case class RemoveEmptyColumns() extends EmptyColumnsStrategy {
      val name = "remove"
      val parameters = Left(Array.empty[Parameter[_]])
    }

    case class RetainEmptyColumns() extends EmptyColumnsStrategy {
      val name = "retain"
      val parameters = Left(Array.empty[Parameter[_]])
    }
  }

  sealed trait MissingValueIndicatorChoice extends Choice {
    import MissingValuesHandlerInfo.MissingValueIndicatorChoice._

    def getIndicatorPrefix: Option[String]
    val choiceOrder: List[ChoiceOption] = List(classOf[Yes], classOf[No])
  }

  object MissingValueIndicatorChoice {

    case class Yes() extends MissingValueIndicatorChoice {
      val name = "Yes"

      val indicatorPrefixParameter = PrefixBasedColumnCreatorParameter("indicator column prefix")
      setDefault(indicatorPrefixParameter, "")
      def getIndicatorPrefix = Some($(indicatorPrefixParameter))
      def setIndicatorPrefix(value: String): this.type = set(indicatorPrefixParameter, value)
      val parameters = Left(Array(indicatorPrefixParameter))
    }

    case class No() extends MissingValueIndicatorChoice {
      val name = "No"
      def getIndicatorPrefix = None
      val parameters = Left(Array.empty[Parameter[_]])
    }
  }
}

case class UserDefinedMissingValue() extends Parameters {

  val missingValueParameter = StringParameter("missing value", validator = RegexValidator.AcceptAll)
  def getMissingValue = $(missingValueParameter)
  def setMissingValue(value: String): this.type = set(missingValueParameter, value)
  setDefault(missingValueParameter, "")
  val parameters = Left(Array(missingValueParameter))

}