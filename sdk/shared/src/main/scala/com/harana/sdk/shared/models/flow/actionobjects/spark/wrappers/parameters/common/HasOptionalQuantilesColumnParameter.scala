package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, Parameters, SingleColumnCreatorParameter}

import scala.language.reflectiveCalls

trait HasOptionalQuantilesColumnParameter extends Parameters {

  val optionalQuantilesColumnParameter = new ChoiceParameter[OptionalQuantilesColumnChoice.QuantilesColumnOption](
    name = "use custom quantiles",
    description = Some("""Param for quantiles column name.
                         |This column will output quantiles of corresponding
                         |quantileProbabilities if it is set.""".stripMargin)
  )
  setDefault(optionalQuantilesColumnParameter, OptionalQuantilesColumnChoice.QuantilesColumnNoOption())
}

object OptionalQuantilesColumnChoice {

  sealed trait QuantilesColumnOption extends Choice {
    val choiceOrder: List[ChoiceOption] = List(
      classOf[QuantilesColumnNoOption],
      classOf[QuantilesColumnYesOption]
    )
  }

  case class QuantilesColumnYesOption() extends QuantilesColumnOption {
    val name = "yes"

    val quantilesColumnParameter = SingleColumnCreatorParameter(
      name = "quantiles column",
      description = Some("The quantiles column for a model."))

    setDefault(quantilesColumnParameter, "quantiles")
    val parameters = Array(quantilesColumnParameter)
  }

  case class QuantilesColumnNoOption() extends QuantilesColumnOption {
    val name = "no"
    val parameters = Array.empty[Parameter[_]]
  }
}
