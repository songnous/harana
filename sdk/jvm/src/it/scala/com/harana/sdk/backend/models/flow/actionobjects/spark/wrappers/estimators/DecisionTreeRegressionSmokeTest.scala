package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.parameters.common.RegressionImpurity.Variance
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.RegressionImpurity.Variance
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class DecisionTreeRegressionSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "DecisionTreeRegression"

  val estimator = new DecisionTreeRegression()
  import estimator._

  val estimatorParameters = Seq[ParameterPair[_]](
    maxDepthParameter            -> 4.0,
    maxBinsParameter             -> 25.0,
    minInstancesPerNodeParameter -> 1.0,
    minInfoGainParameter         -> 0.1,
    maxMemoryInMBParameter       -> 200.0,
    cacheNodeIdsParameter        -> false,
    checkpointIntervalParameter  -> 11.0,
    seedParameter                -> 125.0,
    impurityParameter            -> Variance(),
    featuresColumnParameter      -> NameSingleColumnSelection("myFeatures"),
    labelColumnParameter         -> NameSingleColumnSelection("myLabel"),
    predictionColumnParameter    -> "pred"
  )
}