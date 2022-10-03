package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.RegressionImpurity.Variance
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter

import scala.language.reflectiveCalls

trait HasRegressionImpurityParameter extends Parameters {

  val impurityParameter = ChoiceParameter[RegressionImpurity]("regression-impurity", default = Some(Variance()))

}