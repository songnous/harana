package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import ClassificationImpurity.Gini
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter

import scala.language.reflectiveCalls

trait HasClassificationImpurityParameter extends Parameters {

  val impurityParameter = ChoiceParameter[ClassificationImpurity]("classification impurity", Some("The criterion used for information gain calculation."))

  setDefault(impurityParameter, Gini())
}
