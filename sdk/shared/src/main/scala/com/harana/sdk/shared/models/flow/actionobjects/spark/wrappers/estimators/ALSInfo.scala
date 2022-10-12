package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters._

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

  val alphaParameter = DoubleParameter("alpha", default = Some(1.0), validator = RangeValidator(0.0, Double.PositiveInfinity))
  val implicitPrefsParameter = BooleanParameter("implicit-prefs", default = Some(false))
  val nonNegativeParameter = BooleanParameter("non-negative", default = Some(true))
  val numItemBlocksParameter = IntParameter("num-item-blocks", default = Some(10), validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))
  val numUserBlocksParameter = IntParameter("num-user-blocks", default = Some(10), validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))
  val rankParameter = IntParameter("rank", default = Some(10), validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))

  val ratingColumnParameter = SingleColumnSelectorParameter("rating-column", default = Some(NameSingleColumnSelection("rating")), portIndex = 0)
  def getRatingColumn = $(ratingColumnParameter)

  override val parameterGroups = List(ParameterGroup("",
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
