package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.OptionalQuantilesColumnChoice
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class AFTSurvivalRegressionSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "AFTSurvivalRegression"

  val estimator = new AFTSurvivalRegression()
  import estimator._

  val optionalQuantilesChoice = OptionalQuantilesColumnChoice.QuantilesColumnNoOption()

  val estimatorParameters = Seq(
    censorColumnParameter            -> NameSingleColumnSelection("myCensor"),
    fitInterceptParameter            -> true,
    maxIterationsParameter           -> 2.0,
    toleranceParameter               -> 0.01,
    featuresColumnParameter          -> NameSingleColumnSelection("myStandardizedFeatures"),
    labelColumnParameter             -> NameSingleColumnSelection("myNoZeroLabel"),
    predictionColumnParameter        -> "pred",
    optionalQuantilesColumnParameter -> optionalQuantilesChoice,
    quantileProbabilitiesParameter   -> Array(0.01, 0.05, 0.1, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99)
  )
}