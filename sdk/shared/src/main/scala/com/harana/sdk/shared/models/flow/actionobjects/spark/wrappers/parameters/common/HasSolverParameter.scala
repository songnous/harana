package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.SolverChoice.SolverOption
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, Parameters}

import scala.language.reflectiveCalls

trait HasSolverParameter extends Parameters {

  val solverParameter = ChoiceParameter[SolverOption]("solver", default = Some(SolverChoice.Auto()))

}

object SolverChoice {

  sealed abstract class SolverOption(val name: String) extends Choice {
    override val parameterGroups = List.empty[ParameterGroup]

    val choiceOrder: List[ChoiceOption] = List(
      classOf[Auto],
      classOf[Normal],
      classOf[LBFGS]
    )
  }

  case class Auto() extends SolverOption("auto")
  case class Normal() extends SolverOption("normal")
  case class LBFGS() extends SolverOption("l-bfgs")
}