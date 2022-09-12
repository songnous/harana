package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import SolverChoice.SolverOption
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, Parameters}

import scala.language.reflectiveCalls

trait HasSolverParameter extends Parameters {

  val solverParameter = ChoiceParameter[SolverOption](
    name = "solver",
    description = Some("""Sets the solver algorithm used for optimization.
                         |Can be set to "l-bfgs", "normal" or "auto".
                         |"l-bfgs" denotes Limited-memory BFGS which is a limited-memory quasi-Newton
                         |optimization method. "normal" denotes Normal Equation. It is an analytical
                         |solution to the linear regression problem.
                         |The default value is "auto" which means that the solver algorithm is
                         |selected automatically.""".stripMargin)
  )

  setDefault(solverParameter, SolverChoice.Auto())
}

object SolverChoice {

  sealed abstract class SolverOption(val name: String) extends Choice {
    val parameters = Array.empty[Parameter[_]]

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