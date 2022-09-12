package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.parameters.common.RegressionImpurity.Variance
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.FeatureSubsetStrategy
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class RandomForestRegressionSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "RandomForestRegression"

  val estimator = new RandomForestRegression()
  import estimator._

  val estimatorParameters = Seq(
    maxDepthParameter              -> 5.0,
    maxBinsParameter               -> 32.0,
    minInstancesPerNodeParameter   -> 1.0,
    minInfoGainParameter           -> 0.0,
    maxMemoryInMBParameter         -> 256.0,
    cacheNodeIdsParameter          -> false,
    checkpointIntervalParameter    -> 10.0,
    impurityParameter              -> Variance(),
    subsamplingRateParameter       -> 1.0,
    seedParameter                  -> 1.0,
    numTreesParameter              -> 20.0,
    featureSubsetStrategyParameter -> FeatureSubsetStrategy.Auto(),
    featuresColumnParameter        -> NameSingleColumnSelection("myFeatures"),
    labelColumnParameter           -> NameSingleColumnSelection("myLabel")
  )
}