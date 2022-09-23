package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class MinMaxScalerEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "MinMaxScaler"

  val estimator = new MinMaxScalerEstimator()
  import estimator._

  val estimatorParameters = Seq[ParameterPair[_]](
    minParameter                 -> 0.0,
    maxParameter                 -> 1.0,
    inputColumnParameter         -> NameSingleColumnSelection("myFeatures"),
    singleInPlaceChoiceParameter -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )
}