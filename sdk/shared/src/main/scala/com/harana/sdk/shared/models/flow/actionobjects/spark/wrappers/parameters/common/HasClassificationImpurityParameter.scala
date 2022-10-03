package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.ClassificationImpurity.Gini
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter

import scala.language.reflectiveCalls

trait HasClassificationImpurityParameter extends Parameters {

  val impurityParameter = ChoiceParameter[ClassificationImpurity]("classification-impurity", default = Some(Gini()))

}
