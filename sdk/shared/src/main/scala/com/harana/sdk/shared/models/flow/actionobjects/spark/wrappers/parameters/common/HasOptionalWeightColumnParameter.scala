package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.{NameSingleColumnSelection, SingleColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, Parameters, SingleColumnSelectorParameter}

import scala.language.reflectiveCalls

trait HasOptionalWeightColumnParameter extends Parameters {

  val optionalWeightColumnParameter = new ChoiceParameter[OptionalWeightColumnChoice.WeightColumnOption]("use-custom-weights", default = Some(OptionalWeightColumnChoice.WeightColumnNoOption()))

}

object OptionalWeightColumnChoice {

  sealed trait WeightColumnOption extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[WeightColumnNoOption], classOf[WeightColumnYesOption])
  }

  case class WeightColumnYesOption() extends WeightColumnOption {
    val name = "yes"

    val weightColumnParameter = SingleColumnSelectorParameter("weight-column", default = Some(NameSingleColumnSelection("weight")), portIndex = 0)
    def getWeightColumn = $(weightColumnParameter)
    def setWeightColumn(value: SingleColumnSelection): this.type = set(weightColumnParameter -> value)

    override val parameterGroups = List(ParameterGroup("", weightColumnParameter))
  }

  case class WeightColumnNoOption() extends WeightColumnOption {
    val name = "no"
    override val parameterGroups = List.empty[ParameterGroup]
  }
}