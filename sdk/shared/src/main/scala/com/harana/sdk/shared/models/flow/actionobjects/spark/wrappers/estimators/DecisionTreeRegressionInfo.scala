package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.DecisionTreeParameters
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasLabelColumnParameter, HasRegressionImpurityParameter}

trait DecisionTreeRegressionInfo
    extends EstimatorInfo
    with DecisionTreeParameters
    with HasRegressionImpurityParameter
    with HasLabelColumnParameter {

  val id = "8DE8BDEC-EA24-40BB-9A81-8C62DB59F65E"

  val parameters = Left(Array(
    maxDepthParameter,
    maxBinsParameter,
    minInstancesPerNodeParameter,
    minInfoGainParameter,
    maxMemoryInMBParameter,
    cacheNodeIdsParameter,
    checkpointIntervalParameter,
    seedParameter,
    impurityParameter,
    labelColumnParameter,
    featuresColumnParameter,
    predictionColumnParameter))

}

object DecisionTreeRegressionInfo extends DecisionTreeRegressionInfo