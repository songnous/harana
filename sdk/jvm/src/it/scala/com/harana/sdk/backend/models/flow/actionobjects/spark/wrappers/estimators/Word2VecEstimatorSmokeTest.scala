package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class Word2VecEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "Word2Vec"

  val estimator = new Word2VecEstimator()
  import estimator._

  val estimatorParameters = Seq[ParameterPair[_]](
    inputColumnParameter         -> NameSingleColumnSelection("myStringFeatures"),
    singleInPlaceChoiceParameter -> NoInPlaceChoice().setOutputColumn("testOutputColumn"),
    maxIterationsParameter       -> 2,
    stepSizeParameter            -> 0.25,
    seedParameter                -> 42,
    vectorSizeParameter          -> 99,
    numPartitionsParameter       -> 4,
    minCountParameter            -> 1
  )
}