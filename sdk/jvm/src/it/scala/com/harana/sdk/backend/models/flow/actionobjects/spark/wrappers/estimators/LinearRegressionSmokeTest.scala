package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{OptionalWeightColumnChoice, SolverChoice}
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class LinearRegressionSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "LinearRegression"

  val estimator = new LinearRegression()
  import estimator._

  val weightColumnChoice = OptionalWeightColumnChoice
    .WeightColumnYesOption()
    .setWeightColumn(NameSingleColumnSelection("myWeight"))

  val estimatorParameters = Seq(
    elasticNetParameter           -> 0.8,
    fitInterceptParameter         -> true,
    maxIterationsParameter        -> 2.0,
    regularizationParameter       -> 0.1,
    toleranceParameter            -> 0.01,
    standardizationParameter      -> true,
    featuresColumnParameter       -> NameSingleColumnSelection("myFeatures"),
    labelColumnParameter          -> NameSingleColumnSelection("myLabel"),
    predictionColumnParameter     -> "pred",
    optionalWeightColumnParameter -> weightColumnChoice,
    solverParameter               -> SolverChoice.Auto()
  )

}