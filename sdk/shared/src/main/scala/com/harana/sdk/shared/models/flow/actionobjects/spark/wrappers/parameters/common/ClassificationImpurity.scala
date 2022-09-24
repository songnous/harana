package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import ClassificationImpurity._
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.Choice

sealed abstract class ClassificationImpurity(val name: String) extends Choice {
  val choiceOrder: List[ChoiceOption] = List(
    classOf[Entropy],
    classOf[Gini]
  )

  val parameters = Left(List.empty[Parameter[_]])
}

object ClassificationImpurity {
  case class Entropy() extends ClassificationImpurity("entropy")
  case class Gini() extends ClassificationImpurity("gini")
}