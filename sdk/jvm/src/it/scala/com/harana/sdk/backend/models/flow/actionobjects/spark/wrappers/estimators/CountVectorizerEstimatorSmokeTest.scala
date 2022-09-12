package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class CountVectorizerEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "CountVectorizer"

  val estimator = new CountVectorizerEstimator()
  import estimator._

  val estimatorParameters: Seq[ParameterPair[_]] = Seq(
    inputColumnParameter         -> NameSingleColumnSelection("myStringFeatures"),
    singleInPlaceChoiceParameter -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )
}