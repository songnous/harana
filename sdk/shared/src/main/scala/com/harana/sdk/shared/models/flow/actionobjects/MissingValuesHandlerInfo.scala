package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.MultipleColumnSelection
import com.harana.sdk.shared.models.flow.parameters.validators.AcceptAllRegexValidator
import com.harana.sdk.shared.models.flow.parameters._

trait MissingValuesHandlerInfo extends TransformerInfo {

  import MissingValuesHandlerInfo._

  val id = "A067DB08-3135-4CA7-A49D-BCAA0263C906"

  val selectedColumnsParameter = ColumnSelectorParameter("columns", Some("Columns containing missing values to handle."), portIndex = 0)
  def getSelectedColumns = $(selectedColumnsParameter)
  def setSelectedColumns(value: MultipleColumnSelection): this.type = set(selectedColumnsParameter, value)

  val strategyParameter = ChoiceParameter[Strategy]("strategy", Some("Strategy of handling missing values."))
  setDefault(strategyParameter, Strategy.RemoveRow())
  def getStrategy = $(strategyParameter)
  def setStrategy(value: Strategy): this.type = set(strategyParameter, value)

  val userDefinedMissingValuesParameter = ParametersSequence[UserDefinedMissingValue]("user-defined missing values", Some("Sequence of values to be considered as missing."))
  def getUserDefinedMissingValues = $(userDefinedMissingValuesParameter).map(_.getMissingValue)
  def setUserDefinedMissingValues(value: Seq[String]): this.type = set(userDefinedMissingValuesParameter, value.map(UserDefinedMissingValue().setMissingValue(_)))
  setDefault(userDefinedMissingValuesParameter, Seq(UserDefinedMissingValue().setMissingValue("NA"), UserDefinedMissingValue().setMissingValue("NaN")))

  val missingValueIndicatorParameter = ChoiceParameter[MissingValueIndicatorChoice]("missing value indicator", Some("Generate missing value indicator column."))
  setDefault(missingValueIndicatorParameter, MissingValueIndicatorChoice.No())
  def getMissingValueIndicator = $(missingValueIndicatorParameter)
  def setMissingValueIndicator(value: MissingValueIndicatorChoice): this.type = set(missingValueIndicatorParameter, value)

  val parameters = Array(
    selectedColumnsParameter,
    strategyParameter,
    missingValueIndicatorParameter,
    userDefinedMissingValuesParameter
  )
}

object MissingValuesHandlerInfo extends MissingValuesHandlerInfo {

  sealed trait Strategy extends Choice {
    import MissingValuesHandlerInfo.Strategy._
    val choiceOrder: List[ChoiceOption] = List(classOf[RemoveRow], classOf[RemoveColumn], classOf[ReplaceWithCustomValue], classOf[ReplaceWithMode])
  }

  object Strategy {

    case class RemoveRow() extends Strategy {
      val name = "remove row"
      val parameters = Array.empty[Parameter[_]]
    }

    case class RemoveColumn() extends Strategy {
      val name = "remove column"
      val parameters = Array.empty[Parameter[_]]
    }

    case class ReplaceWithCustomValue() extends Strategy {
      val name = "replace with custom value"
      val customValueParameter = StringParameter("value", Some("Replacement for missing values."))
      def getCustomValue = $(customValueParameter)
      def setCustomValue(value: String): this.type = set(customValueParameter, value)

      val parameters = Array(customValueParameter)
    }

    case class ReplaceWithMode() extends Strategy {
      val name = "replace with mode"
      val emptyColumnStrategyParameter = ChoiceParameter[EmptyColumnsStrategy]("empty column strategy", Some("Strategy of handling columns with missing all values."))
      setDefault(emptyColumnStrategyParameter, EmptyColumnsStrategy.RemoveEmptyColumns())
      def getEmptyColumnStrategy = $(emptyColumnStrategyParameter)
      def setEmptyColumnStrategy(value: EmptyColumnsStrategy): this.type = set(emptyColumnStrategyParameter, value)

      val parameters = Array(emptyColumnStrategyParameter)
    }
  }

  sealed trait EmptyColumnsStrategy extends Choice {
    import MissingValuesHandlerInfo.EmptyColumnsStrategy._
    val choiceOrder: List[ChoiceOption] = List(classOf[RemoveEmptyColumns], classOf[RetainEmptyColumns])
  }

  object EmptyColumnsStrategy {

    case class RemoveEmptyColumns() extends EmptyColumnsStrategy {
      val name = "remove"
      val parameters = Array.empty[Parameter[_]]
    }

    case class RetainEmptyColumns() extends EmptyColumnsStrategy {
      val name = "retain"
      val parameters = Array.empty[Parameter[_]]
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

      val indicatorPrefixParameter = PrefixBasedColumnCreatorParameter("indicator column prefix", Some("Prefix for columns indicating presence of missing values."))
      setDefault(indicatorPrefixParameter, "")
      def getIndicatorPrefix = Some($(indicatorPrefixParameter))
      def setIndicatorPrefix(value: String): this.type = set(indicatorPrefixParameter, value)
      val parameters = Array(indicatorPrefixParameter)
    }

    case class No() extends MissingValueIndicatorChoice {
      val name = "No"
      def getIndicatorPrefix = None
      val parameters = Array.empty[Parameter[_]]
    }
  }
}

case class UserDefinedMissingValue() extends Parameters {

  val missingValueParameter = StringParameter("missing value",
    description = Some("""Value to be considered as a missing one.
                         |Provided value will be cast to all chosen column types if possible,
                         |so for example a value "-1" might be applied to all numeric and string columns.""".stripMargin),
    validator = new AcceptAllRegexValidator()
  )

  def getMissingValue = $(missingValueParameter)
  def setMissingValue(value: String): this.type = set(missingValueParameter, value)
  setDefault(missingValueParameter, "")
  val parameters = Array(missingValueParameter)
}