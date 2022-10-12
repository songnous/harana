package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.AFTSurvivalRegressionParameters
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, SingleColumnSelectorParameter}

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

  val censorColumnParameter = SingleColumnSelectorParameter("censor-column", default = Some(NameSingleColumnSelection("censor")), portIndex = 0)

  override val parameterGroups = List(ParameterGroup("",
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