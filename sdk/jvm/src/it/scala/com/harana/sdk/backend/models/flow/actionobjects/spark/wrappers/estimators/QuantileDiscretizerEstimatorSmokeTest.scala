package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class QuantileDiscretizerEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "QuantileDiscretizer"

  val estimator = new QuantileDiscretizerEstimator()
  import estimator._

  val estimatorParameters = Seq(
    numBucketsParameter -> 2,
    inputColumnParameter -> NameSingleColumnSelection("myRating"),
    singleInPlaceChoiceParameter -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )
}