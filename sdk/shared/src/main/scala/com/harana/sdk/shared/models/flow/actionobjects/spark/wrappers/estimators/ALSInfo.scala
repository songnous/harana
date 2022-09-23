package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, DoubleParameter, IntParameter, SingleColumnSelectorParameter}

trait ALSInfo
  extends EstimatorInfo
    with HasItemColumnParameter
    with HasPredictionColumnCreatorParameter
    with HasUserColumnParameter
    with HasMaxIterationsParameter
    with HasSeedParameter
    with HasRegularizationParameter
    with HasCheckpointIntervalParameter {

  val id = "13B66409-18FA-4AF0-B7CA-8EA657A36054"

  val alphaParameter = DoubleParameter("alpha", validator = RangeValidator(0.0, Double.PositiveInfinity))
  setDefault(alphaParameter, 1.0)

  val implicitPrefsParameter = BooleanParameter("implicit prefs")
  setDefault(implicitPrefsParameter, false)

  val nonNegativeParameter = BooleanParameter("nonnegative")
  setDefault(nonNegativeParameter, true)

  val numItemBlocksParameter = IntParameter("num item blocks", validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))
  setDefault(numItemBlocksParameter, 10)

  val numUserBlocksParameter = IntParameter("num user blocks", validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))
  setDefault(numUserBlocksParameter, 10)

  val rankParameter = IntParameter("rank", validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))
  setDefault(rankParameter, 10)

  val ratingColumnParameter = SingleColumnSelectorParameter("rating column", portIndex = 0)
  def getRatingColumn = $(ratingColumnParameter)
  setDefault(ratingColumnParameter, NameSingleColumnSelection("rating"))

  val parameters = Left(Array(
    alphaParameter,
    checkpointIntervalParameter,
    implicitPrefsParameter,
    maxIterationsParameter,
    nonNegativeParameter,
    numItemBlocksParameter,
    numUserBlocksParameter,
    rankParameter,
    ratingColumnParameter,
    regularizationParameter,
    seedParameter,
    itemColumnParameter,
    predictionColumnParameter,
    userColumnParameter))
}

object ALSInfo extends ALSInfo
