package com.harana.sdk.shared.models.flow.actiontypes.inout

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RegexValidator
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameter, ParameterGroup, Parameters, StringParameter}

trait NamesIncludedParameter { this: Parameters =>
  val namesIncludedParameter = BooleanParameter("names-included", default = Some(true))
  def getNamesIncluded = $(namesIncludedParameter)
  def setNamesIncluded(value: Boolean): this.type = set(namesIncludedParameter, value)
}

trait CsvParameters extends NamesIncludedParameter { this: Parameters =>
  import CsvParameters._

  val csvColumnSeparatorParameter = ChoiceParameter[ColumnSeparatorChoice]("separator", default = Some(ColumnSeparatorChoice.Comma()))
  def getCsvColumnSeparator = $(csvColumnSeparatorParameter)
  def setCsvColumnSeparator(value: ColumnSeparatorChoice): this.type = set(csvColumnSeparatorParameter, value)
  def determineColumnSeparator = CsvParameters.determineColumnSeparatorOf(getCsvColumnSeparator)
}

object CsvParameters {

  def determineColumnSeparatorOf(choice: ColumnSeparatorChoice): Char = {
    choice match {
      case ColumnSeparatorChoice.Comma()              => ','
      case ColumnSeparatorChoice.Semicolon()          => ';'
      case ColumnSeparatorChoice.Tab()                => '\t'
      case ColumnSeparatorChoice.Colon()              => ':'
      case ColumnSeparatorChoice.Space()              => ' '
      case customChoice: ColumnSeparatorChoice.Custom => customChoice.getCustomColumnSeparator(0)
    }
  }

  sealed trait ColumnSeparatorChoice extends Choice {
    import ColumnSeparatorChoice._

    val choiceOrder: List[ChoiceOption] = List(classOf[Comma], classOf[Semicolon], classOf[Colon], classOf[Space], classOf[Tab], classOf[Custom])
  }

  object ColumnSeparatorChoice {

    case class Comma() extends ColumnSeparatorChoice {
      val name = "comma"
      override val parameterGroups = List.empty[ParameterGroup]
    }

    case class Semicolon() extends ColumnSeparatorChoice {
      val name = "semicolon"
      override val parameterGroups = List.empty[ParameterGroup]
    }

    case class Colon() extends ColumnSeparatorChoice {
      val name = "colon"
      override val parameterGroups = List.empty[ParameterGroup]
    }

    case class Space() extends ColumnSeparatorChoice {
      val name = "space"
      override val parameterGroups = List.empty[ParameterGroup]
    }

    case class Tab() extends ColumnSeparatorChoice {
      val name = "tab"
      override val parameterGroups = List.empty[ParameterGroup]
    }

    case class Custom() extends ColumnSeparatorChoice {
      val name = "custom"

      val customColumnSeparator = StringParameter("custom-separator", default = Some("comma"), validator = RegexValidator.SingleChar)
      def getCustomColumnSeparator = $(customColumnSeparator)
      def setCustomColumnSeparator(value: String): this.type = set(customColumnSeparator, value)

      override val parameterGroups = List(ParameterGroup("", customColumnSeparator))
    }
  }
}
