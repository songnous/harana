package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.NaiveBayes.Multinomial
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class NaiveBayesSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "NaiveBayes"

  val estimator = new NaiveBayes()
  import estimator._

  val estimatorParameters = Seq[ParameterPair[_]](
    smoothingParameter           -> 1.0,
    modelTypeParameter           -> Multinomial(),
    featuresColumnParameter      -> NameSingleColumnSelection("myFeatures"),
    labelColumnParameter         -> NameSingleColumnSelection("myLabel"),
    probabilityColumnParameter   -> "prob",
    rawPredictionColumnParameter -> "rawPred",
    predictionColumnParameter    -> "pred"
  )
}