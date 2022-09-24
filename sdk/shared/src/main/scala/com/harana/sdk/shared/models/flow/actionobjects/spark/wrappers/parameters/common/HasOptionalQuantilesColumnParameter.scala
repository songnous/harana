package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, Parameters, SingleColumnCreatorParameter}

import scala.language.reflectiveCalls

trait HasOptionalQuantilesColumnParameter extends Parameters {

  val optionalQuantilesColumnParameter = new ChoiceParameter[OptionalQuantilesColumnChoice.QuantilesColumnOption]("use custom quantiles",
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
    val quantilesColumnParameter = SingleColumnCreatorParameter("quantiles column", default = Some("quantiles"))
    val parameters = Left(Array(quantilesColumnParameter))
  }

  case class QuantilesColumnNoOption() extends QuantilesColumnOption {
    val name = "no"
    val parameters = Left(Array.empty[Parameter[_]])
  }
}
