package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class MultilayerPerceptronClassifierSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "MultilayerPerceptronClassifier"

  val estimator = new MultilayerPerceptronClassifier()
  import estimator._

  val estimatorParameters = Seq(
    featuresColumnParameter   -> NameSingleColumnSelection("myFeatures"),
    labelColumnParameter      -> NameSingleColumnSelection("myRating"),
    layersParameter           -> Array(3.0, 2.0, 1.0),
    maxIterationsParameter    -> 120.0,
    predictionColumnParameter -> "prediction",
    seedParameter             -> 100.0,
    toleranceParameter        -> 2e-5
  )
}