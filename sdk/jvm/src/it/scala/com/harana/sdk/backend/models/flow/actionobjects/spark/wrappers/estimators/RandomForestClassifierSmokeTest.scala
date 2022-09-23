package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{ClassificationImpurity, FeatureSubsetStrategy}
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class RandomForestClassifierSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "RandomForestClassifier"

  val estimator = new RandomForestClassifier()
  import estimator.vanillaRandomForestClassifier._

  val estimatorParameters = Seq[ParameterPair[_]](
    maxDepthParameter              -> 3,
    maxBinsParameter               -> 40,
    impurityParameter              -> ClassificationImpurity.Entropy(),
    featuresColumnParameter        -> NameSingleColumnSelection("myFeatures"),
    labelColumnParameter           -> NameSingleColumnSelection("myLabel"),
    minInstancesPerNodeParameter   -> 1,
    minInfoGainParameter           -> 2,
    maxMemoryInMBParameter         -> 20,
    cacheNodeIdsParameter          -> true,
    checkpointIntervalParameter    -> 3,
    subsamplingRateParameter       -> 0.5,
    seedParameter                  -> 555,
    numTreesParameter              -> 30,
    featureSubsetStrategyParameter -> FeatureSubsetStrategy.Auto()
  )
}