package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.ClassificationImpurity._
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup
import com.harana.sdk.shared.models.flow.parameters.choice.Choice
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption

sealed abstract class ClassificationImpurity(val name: String) extends Choice {
  val choiceOrder: List[ChoiceOption] = List(
    classOf[Entropy],
    classOf[Gini]
  )

  override val parameterGroups = List.empty[ParameterGroup]
}

object ClassificationImpurity {
  case class Entropy() extends ClassificationImpurity("entropy")
  case class Gini() extends ClassificationImpurity("gini")
}