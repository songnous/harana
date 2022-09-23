package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.parameters.common.ClassificationImpurity.Gini
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.ClassificationImpurity.Gini
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class DecisionTreeClassifierSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "DecisionTreeClassifier"

  val estimator = new DecisionTreeClassifier()
  import estimator.vanillaDecisionTreeClassifier._

  val estimatorParameters = Seq[ParameterPair[_]](
    maxDepthParameter            -> 6.0,
    maxBinsParameter             -> 28.0,
    minInstancesPerNodeParameter -> 2.0,
    minInfoGainParameter         -> 0.05,
    maxMemoryInMBParameter       -> 312.0,
    cacheNodeIdsParameter        -> false,
    checkpointIntervalParameter  -> 8.0,
    seedParameter                -> 12345.0,
    impurityParameter            -> Gini(),
    featuresColumnParameter      -> NameSingleColumnSelection("myFeatures"),
    labelColumnParameter         -> NameSingleColumnSelection("myLabel"),
    probabilityColumnParameter   -> "prob",
    rawPredictionColumnParameter -> "rawPred",
    predictionColumnParameter    -> "pred"
  )
}