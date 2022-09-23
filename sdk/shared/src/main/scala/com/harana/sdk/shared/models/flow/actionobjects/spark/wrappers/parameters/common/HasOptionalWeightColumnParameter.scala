package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.{NameSingleColumnSelection, SingleColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, Parameters, SingleColumnSelectorParameter}

import scala.language.reflectiveCalls

trait HasOptionalWeightColumnParameter extends Parameters {

  val optionalWeightColumnParameter = new ChoiceParameter[OptionalWeightColumnChoice.WeightColumnOption]("use custom weights")
  setDefault(optionalWeightColumnParameter, OptionalWeightColumnChoice.WeightColumnNoOption())

}

object OptionalWeightColumnChoice {

  sealed trait WeightColumnOption extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[WeightColumnNoOption], classOf[WeightColumnYesOption])
  }

  case class WeightColumnYesOption() extends WeightColumnOption {
    val name = "yes"

    val weightColumnParameter = SingleColumnSelectorParameter("weight column", portIndex = 0)
    setDefault(weightColumnParameter, NameSingleColumnSelection("weight"))
    def getWeightColumn = $(weightColumnParameter)
    def setWeightColumn(value: SingleColumnSelection): this.type = set(weightColumnParameter -> value)

    val parameters = Left(Array(weightColumnParameter))
  }

  case class WeightColumnNoOption() extends WeightColumnOption {
    val name = "no"
    val parameters = Left(Array.empty[Parameter[_]])
  }
}