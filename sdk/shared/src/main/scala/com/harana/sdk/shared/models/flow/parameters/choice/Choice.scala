package com.harana.sdk.shared.models.flow.parameters.choice

import Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.Parameters

trait Choice extends Parameters {

  val name: String

  val choiceOrder: List[ChoiceOption]

}

object Choice {
  type ChoiceOption = Class[_ <: Choice]
}