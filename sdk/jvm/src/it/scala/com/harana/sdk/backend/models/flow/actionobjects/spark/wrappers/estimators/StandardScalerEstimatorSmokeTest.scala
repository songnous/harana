package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class StandardScalerEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "StandardScaler"

  val estimator = new StandardScalerEstimator()
  import estimator._

  val estimatorParameters = Seq[ParameterPair[_]](
    withMeanParameter            -> false,
    withStdParameter             -> true,
    inputColumnParameter         -> NameSingleColumnSelection("myFeatures"),
    singleInPlaceChoiceParameter -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )
}