package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class VectorIndexerEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "VectorIndexer"

  val estimator = new VectorIndexerEstimator()
  import estimator._

  val estimatorParameters = Seq[ParameterPair[_]](
    maxCategoriesParameter       -> 2,
    inputColumnParameter         -> NameSingleColumnSelection("myFeatures"),
    singleInPlaceChoiceParameter -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )
}