package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.{NameSingleColumnSelection, SingleColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, Parameters, SingleColumnSelectorParameter}

import scala.language.reflectiveCalls

trait HasOptionalWeightColumnParameter extends Parameters {

  val optionalWeightColumnParameter = new ChoiceParameter[OptionalWeightColumnChoice.WeightColumnOption](
    name = "use custom weights",
    description = Some("""Whether to over-/under-sample training instances according to the given weights in
                         |the `weight column`. If the `weight column` is not specified,
                         |all instances are treated equally with a weight 1.0.""".stripMargin)
  )

  setDefault(optionalWeightColumnParameter, OptionalWeightColumnChoice.WeightColumnNoOption())

}

object OptionalWeightColumnChoice {

  sealed trait WeightColumnOption extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[WeightColumnNoOption], classOf[WeightColumnYesOption])
  }

  case class WeightColumnYesOption() extends WeightColumnOption {
    val name = "yes"

    val weightColumnParameter = SingleColumnSelectorParameter(
      name = "weight column",
      description = Some("The weight column for a model."),
      portIndex = 0
    )

    setDefault(weightColumnParameter, NameSingleColumnSelection("weight"))
    def getWeightColumn = $(weightColumnParameter)
    def setWeightColumn(value: SingleColumnSelection): this.type = set(weightColumnParameter -> value)

    val parameters = Array(weightColumnParameter)
  }

  case class WeightColumnNoOption() extends WeightColumnOption {
    val name = "no"
    val parameters = Array.empty[Parameter[_]]
  }
}