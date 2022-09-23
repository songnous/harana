package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.OptionalWeightColumnChoice
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class LogisticRegressionSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "LogisticRegression"

  val estimator = new LogisticRegression()
  import estimator._

  val weightColumnChoice = OptionalWeightColumnChoice
    .WeightColumnYesOption()
    .setWeightColumn(NameSingleColumnSelection("myWeight"))

  val estimatorParameters = Seq[ParameterPair[_]](
    elasticNetParameter           -> 0.8,
    fitInterceptParameter         -> true,
    maxIterationsParameter        -> 2.0,
    regularizationParameter       -> 0.1,
    toleranceParameter            -> 0.01,
    standardizationParameter      -> true,
    featuresColumnParameter       -> NameSingleColumnSelection("myFeatures"),
    labelColumnParameter          -> NameSingleColumnSelection("myLabel"),
    probabilityColumnParameter    -> "prob",
    rawPredictionColumnParameter  -> "rawPred",
    predictionColumnParameter     -> "pred",
    thresholdParameter            -> 0.3,
    optionalWeightColumnParameter -> weightColumnChoice
  )
}