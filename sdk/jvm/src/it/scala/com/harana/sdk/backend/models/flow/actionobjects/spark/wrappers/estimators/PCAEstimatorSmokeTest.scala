package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class PCAEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "PCA"

  val estimator = new PCAEstimator()
  import estimator._

  val estimatorParameters = Seq(
    kParameter                   -> 2,
    inputColumnParameter         -> NameSingleColumnSelection("myFeatures"),
    singleInPlaceChoiceParameter -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )
}