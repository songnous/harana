package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.Choice

sealed abstract class RegressionImpurity(val name: String) extends Choice {
  import RegressionImpurity._

  val parameters = Left(Array.empty[Parameter[_]])

  val choiceOrder: List[ChoiceOption] = List(
    classOf[Variance]
  )
}

object RegressionImpurity {
  case class Variance() extends RegressionImpurity("variance")
}
