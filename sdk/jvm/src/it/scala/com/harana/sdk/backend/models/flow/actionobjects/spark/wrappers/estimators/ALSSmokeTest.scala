package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class ALSSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  def className = "ALS"

  val estimator = new ALS()
  import estimator._

  val estimatorParameters = Seq[ParameterPair[_]](
    alphaParameter               -> 1.0,
    checkpointIntervalParameter  -> 15.0,
    implicitPrefsParameter       -> false,
    itemColumnParameter          -> NameSingleColumnSelection("myItemId"),
    maxIterationsParameter       -> 5.0,
    nonNegativeParameter         -> false,
    numItemBlocksParameter       -> 10.0,
    numUserBlocksParameter       -> 10.0,
    predictionColumnParameter    -> "prediction",
    rankParameter                -> 8.0,
    ratingColumnParameter        -> NameSingleColumnSelection("myRating"),
    regularizationParameter      -> 0.2,
    seedParameter                -> 100.0,
    userColumnParameter          -> NameSingleColumnSelection("myUserId")
  )
}