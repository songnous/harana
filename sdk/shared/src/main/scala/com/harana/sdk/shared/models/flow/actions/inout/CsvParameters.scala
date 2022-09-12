package com.harana.sdk.shared.models.flow.actions.inout

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameter, Parameters, StringParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.SingleCharRegexValidator

trait NamesIncludedParameter { this: Parameters =>
  val namesIncludedParameter = BooleanParameter("names included", Some("Does the first row include column names?"))
  setDefault(namesIncludedParameter, true)
  def getNamesIncluded = $(namesIncludedParameter)
  def setNamesIncluded(value: Boolean): this.type = set(namesIncludedParameter, value)
}

trait CsvParameters extends NamesIncludedParameter { this: Parameters =>
  import CsvParameters._

  val csvColumnSeparatorParameter = ChoiceParameter[ColumnSeparatorChoice]("separator", Some("Column separator."))
  setDefault(csvColumnSeparatorParameter, ColumnSeparatorChoice.Comma())
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
      val name = ","
      val parameters = Array.empty[Parameter[_]]
    }

    case class Semicolon() extends ColumnSeparatorChoice {
      val name = ";"
      val parameters = Array.empty[Parameter[_]]
    }

    case class Colon() extends ColumnSeparatorChoice {
      val name = ":"
      val parameters = Array.empty[Parameter[_]]
    }

    case class Space() extends ColumnSeparatorChoice {
      val name = "Space"
      val parameters = Array.empty[Parameter[_]]
    }

    case class Tab() extends ColumnSeparatorChoice {
      val name = "Tab"
      val parameters = Array.empty[Parameter[_]]
    }

    case class Custom() extends ColumnSeparatorChoice {
      val name = "Custom"

      val customColumnSeparator = StringParameter(name = "custom separator", description = None, validator = new SingleCharRegexValidator)
      setDefault(customColumnSeparator, ",")
      def getCustomColumnSeparator = $(customColumnSeparator)
      def setCustomColumnSeparator(value: String): this.type = set(customColumnSeparator, value)

      val parameters = Array(customColumnSeparator)
    }
  }
}
