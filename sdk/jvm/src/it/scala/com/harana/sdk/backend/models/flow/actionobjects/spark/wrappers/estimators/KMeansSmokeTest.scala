package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class KMeansSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "KMeans"

  val estimator = new KMeans()
  import estimator._

  val estimatorParameters = Seq(
    featuresColumnParameter   -> NameSingleColumnSelection("myFeatures"),
    kParameter                -> 3.0,
    maxIterationsParameter    -> 20.0,
    predictionColumnParameter -> "cluster",
    seedParameter             -> 123.0,
    toleranceParameter        -> 1e-7,
    initModeParameter         -> KMeans.ParallelInitMode(),
    initStepsParameter          -> 8
  )
}