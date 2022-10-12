package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, Parameters, SingleColumnCreatorParameter}

import scala.language.reflectiveCalls

trait HasOptionalQuantilesColumnParameter extends Parameters {

  val optionalQuantilesColumnParameter = new ChoiceParameter[OptionalQuantilesColumnChoice.QuantilesColumnOption]("use-custom-quantiles",
    default = Some(OptionalQuantilesColumnChoice.QuantilesColumnNoOption())
  )

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
    val quantilesColumnParameter = SingleColumnCreatorParameter("quantiles-column", default = Some("quantiles"))
    override val parameterGroups = List(ParameterGroup("", quantilesColumnParameter))
  }

  case class QuantilesColumnNoOption() extends QuantilesColumnOption {
    val name = "no"
    override val parameterGroups = List.empty[ParameterGroup]
  }
}
