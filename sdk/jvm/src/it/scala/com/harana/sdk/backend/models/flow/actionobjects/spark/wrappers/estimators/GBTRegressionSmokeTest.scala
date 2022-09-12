package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.parameters.common.RegressionImpurity.Variance
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class GBTRegressionSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "GBTRegression"

  private val labelColumnName = "myRating"

  val estimator = new GBTRegression()
  import estimator._

  val estimatorParameters = Seq(
    featuresColumnParameter      -> NameSingleColumnSelection("myFeatures"),
    impurityParameter            -> Variance(),
    labelColumnParameter         -> NameSingleColumnSelection(labelColumnName),
    lossTypeParameter            -> GBTRegression.Squared(),
    maxBinsParameter             -> 2.0,
    maxDepthParameter            -> 6.0,
    maxIterationsParameter       -> 10.0,
    minInfoGainParameter         -> 0.0,
    minInstancesPerNodeParameter -> 1,
    predictionColumnParameter    -> "prediction",
    seedParameter                -> 100.0,
    stepSizeParameter            -> 0.11,
    subsamplingRateParameter     -> 0.999
  )
}