package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class IDFEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "IDF"

  val estimator = new IDFEstimator()
  import estimator._

  val estimatorParameters = Seq(
    minDocFreqParameter          -> 0,
    inputColumnParameter         -> NameSingleColumnSelection("myFeatures"),
    singleInPlaceChoiceParameter -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )
}