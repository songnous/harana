package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.AFTSurvivalRegressionParameters
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.parameters.SingleColumnSelectorParameter
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

import scala.language.reflectiveCalls

trait AFTSurvivalRegressionInfo
    extends EstimatorInfo
    with AFTSurvivalRegressionParameters
    with HasLabelColumnParameter
    with HasMaxIterationsParameter
    with HasOptionalQuantilesColumnParameter
    with HasToleranceParameter
    with HasFitInterceptParameter {

  val id = "6AB9BCA9-B914-49C0-A1BC-770287F57EFB"

  val censorColumnParameter = SingleColumnSelectorParameter("censor column", portIndex = 0)
  setDefault(censorColumnParameter, NameSingleColumnSelection("censor"))

  val parameters = Left(Array(
    fitInterceptParameter,
    maxIterationsParameter,
    toleranceParameter,
    labelColumnParameter,
    censorColumnParameter,
    featuresColumnParameter,
    predictionColumnParameter,
    quantileProbabilitiesParameter,
    optionalQuantilesColumnParameter))
}

object AFTSurvivalRegressionInfo extends AFTSurvivalRegressionInfo