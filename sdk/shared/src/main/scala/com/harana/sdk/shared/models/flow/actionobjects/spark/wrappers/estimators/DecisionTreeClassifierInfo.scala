package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.DecisionTreeParameters
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasClassificationImpurityParameter, HasLabelColumnParameter, ProbabilisticClassifierParameters}
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

trait DecisionTreeClassifierInfo
    extends EstimatorInfo
    with HasClassificationImpurityParameter
    with DecisionTreeParameters
    with ProbabilisticClassifierParameters
    with HasLabelColumnParameter {

  val id = "2F39BEFD-FC2B-4034-B13F-D308BB0C8078"

  override val parameterGroups = List(ParameterGroup("",
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
    probabilityColumnParameter,
    rawPredictionColumnParameter,
    predictionColumnParameter
  ))
}

object DecisionTreeClassifierInfo extends DecisionTreeClassifierInfo