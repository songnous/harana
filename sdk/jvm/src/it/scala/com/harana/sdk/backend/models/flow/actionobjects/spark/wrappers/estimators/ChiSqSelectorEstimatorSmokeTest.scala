package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class ChiSqSelectorEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "ChiSqSelector"

  val estimator = new UnivariateFeatureSelectorEstimator()
  import estimator._

  val estimatorParameters = Seq(
    numTopFeaturesParameter -> 2,
    featuresColumnParameter -> NameSingleColumnSelection("myFeatures"),
    labelColumnParameter    -> NameSingleColumnSelection("myLabel"),
    outputColumnParameter   -> "output"
  )
}